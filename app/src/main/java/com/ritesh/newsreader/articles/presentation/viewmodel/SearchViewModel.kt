package com.ritesh.newsreader.articles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.network.NoInternetException
import com.ritesh.newsreader.common.ui.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: ArticleRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper
) :
    ViewModel() {

    private var searchPageNum = AppConstants.DEFAULT_PAGE_NUM
    private val _searchNewsItem = MutableStateFlow<UIState<List<Article>>>(UIState.Empty)
    val searchNewsItem: StateFlow<UIState<List<Article>>> = _searchNewsItem
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    init {
        viewModelScope.launch {
            _query
                .debounce(AppConstants.SEARCH_NEWS_TIME_DELAY)
                .filter {
                    return@filter it.isNotEmpty()
                }
                .distinctUntilChanged()
                .flatMapLatest { searchQuery ->
                    if (!networkHelper.isNetworkConnected()) {
                        throw NoInternetException()
                    }
                    _searchNewsItem.emit(UIState.Loading)
                    newsRepository.searchNews(searchQuery = searchQuery, pageNumber = searchPageNum)
                        .catch {
                            _searchNewsItem.emit(UIState.Failure(it))
                        }
                }
                .map { item ->
                    item.apply {
                        this.filter {
                            it.title?.isNotEmpty() == true && !it.urlToImage.isNullOrEmpty()
                        }
                    }
                }
                .flowOn(dispatcherProvider.io)
                .catch {
                    _searchNewsItem.emit(UIState.Failure(it))
                }
                .collect {
                    _searchNewsItem.emit(UIState.Success(it))
                }
        }
    }

    fun searchNews(searchQuery: String = _query.value) {
        _query.value = searchQuery
    }

}