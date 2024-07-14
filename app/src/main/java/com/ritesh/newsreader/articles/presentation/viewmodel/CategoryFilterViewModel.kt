package com.ritesh.newsreader.articles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.newsreader.articles.data.model.Category
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.network.NoInternetException
import com.ritesh.newsreader.common.ui.base.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryFilterViewModel @Inject constructor(
    private val newsRepository: ArticleRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _categoryItem = MutableStateFlow<UIState<List<Category>>>(UIState.Empty)
    val categoryItem: StateFlow<UIState<List<Category>>> = _categoryItem

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                _categoryItem.emit(
                    UIState.Failure(
                        throwable = NoInternetException()
                    )
                )
                return@launch
            }
            _categoryItem.emit(UIState.Loading)
            newsRepository.getCategories()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _categoryItem.emit(UIState.Failure(it))
                }
                .collect {
                    _categoryItem.emit(UIState.Success(it))
                }
        }
    }
}