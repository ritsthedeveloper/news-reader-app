package com.ritesh.newsreader.articles.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.application.ArticlesUseCase
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.network.NoInternetException
import com.ritesh.newsreader.common.ui.base.UIState
import com.ritesh.newsreader.logger.Logger
import com.ritesh.newsreader.util.ValidationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModelV2 @Inject constructor(
    private val articlesUseCase: ArticlesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val pager: Pager<Int, Article>,
    private val networkHelper: NetworkHelper,
    private val dispatcherProvider: DispatcherProvider,
    val logger: Logger
) : ViewModel() {

    private val _tag : String = "NewsViewModelV2"
    private val _newsItem = MutableStateFlow<UIState<List<Article>>>(UIState.Empty)
    val newsItem: StateFlow<UIState<List<Article>>> = _newsItem

    // PagingData is the component that connects the ViewModel layer to the UI.
    // It queries a PagingSource object and stores the result.
    private val _newsItemPaging = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val newsItemPaging: StateFlow<PagingData<Article>> = _newsItemPaging

    init {
        fetchNews()
    }

    fun fetchNews() {
        logger.logDebug(_tag, "Inside fetchNews")
        when {
            ValidationUtil.checkIfValidArgNews(savedStateHandle["country"]) -> {
                fetchNewsByCountry(savedStateHandle["country"])
            }

            ValidationUtil.checkIfValidArgNews(savedStateHandle["language"]) -> {
                fetchNewsByLanguage(savedStateHandle["language"])
            }

            ValidationUtil.checkIfValidArgNews(savedStateHandle["source"]) -> {
                fetchNewsBySource(savedStateHandle["source"])
            }

            ValidationUtil.checkIfValidArgNews(savedStateHandle["category"]) -> {
                fetchNewsByCategory(savedStateHandle["category"])
            }

            else -> {
                fetchNewsWithoutFilter()
            }
        }
    }

    /**
     * As we need a stream of paged data from the PagingSource implementation. We are setting up the data stream in
     * our ViewModel(flow in this case). The Pager class provides methods that expose a reactive stream of PagingData
     * objects from a PagingSource.
     */
    private fun fetchNewsWithoutFilter() {
        logger.logDebug(_tag, "Inside fetchNewsWithoutFilter")
        // ViewModel scope is a managed scope and hence it avoids the leaks
        viewModelScope.launch {
            pager.flow
                // Map the outer stream so that the transformations are applied to
                // each new generation of PagingData.
                .map {
                    // Returns a PagingData containing only elements matching the given predicate.
                    // The predicate in this case is non-empty title and thumbnailUrl of the news-article item
                    it.filter { article ->
                        article.title?.isNotEmpty() == true &&
                                article.urlToImage?.isNotEmpty() == true
                    }
                }
                // cachedIn operator makes the data stream shareable and caches the loaded data with ViewModelScope
                .cachedIn(viewModelScope)
                .collect {
                    _newsItemPaging.value = it // Collect the transformed values (State).
                }
        }
    }

    /**
     * Fetch the news articles as per the country selected by user.
     */
    private fun fetchNewsByCountry(countryId: String?) {
        logger.logDebug(_tag, "Inside fetchNewsByCountry")
        viewModelScope.launch {
            setupBeforeRequest()
            // Make the request
            articlesUseCase
                .getNewsByCountry(countryId ?: AppConstants.DEFAULT_COUNTRY)
                .mapFilterCollectNews()
        }
    }

    /**
     * Fetch the news articles as per the language selected by user.
     */
    private fun fetchNewsByLanguage(languageId: String?) {
        logger.logDebug(_tag, "Inside fetchNewsByLanguage")
        viewModelScope.launch {
            setupBeforeRequest()
            // Make the request
            articlesUseCase
                .getNewsByLanguage(languageId ?: AppConstants.DEFAULT_LANGUAGE)
                .mapFilterCollectNews()
        }
    }

    private fun fetchNewsBySource(sourceId: String?) {
        logger.logDebug(_tag, "Inside fetchNewsBySource")
        viewModelScope.launch {
            setupBeforeRequest()
            articlesUseCase
                .getNewsBySource(sourceId ?: AppConstants.DEFAULT_SOURCE)
                .mapFilterCollectNews()
        }
    }

    private fun fetchNewsByCategory(categoryId: String?) {
        logger.logDebug(_tag, "Inside fetchNewsByCategory")
        viewModelScope.launch {
            setupBeforeRequest()
            articlesUseCase
                .getNewsByCategory(categoryId ?: AppConstants.DEFAULT_CATEGORY)
                .mapFilterCollectNews()
        }
    }

    private suspend fun setupBeforeRequest() {
        logger.logDebug(_tag, "Inside setupBeforeRequest")
        // Check for network availability
        if (!networkHelper.isNetworkConnected()) {
            _newsItem.emit(
                UIState.Failure(
                    throwable = NoInternetException()
                )
            )
            return
        }
        // Update the UI with loading indicator
        _newsItem.emit(UIState.Loading)
    }

    private suspend fun Flow<List<Article>>.mapFilterCollectNews() {
        this.map { item ->
            item.apply {
                this.filter {
                    it.title?.isNotEmpty() == true &&
                            it.urlToImage?.isNotEmpty() == true
                }
            }
        }
            .flowOn(dispatcherProvider.io)
            .catch {
                _newsItem.emit(UIState.Failure(it))
            }
            .collect {
                _newsItem.emit(UIState.Success(it))
                logger.logDebug(_tag, "Success")
            }
    }
}