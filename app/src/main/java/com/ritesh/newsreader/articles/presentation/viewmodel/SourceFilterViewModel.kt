package com.ritesh.newsreader.articles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
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
class SourceFilterViewModel @Inject constructor(
    private val newsRepository: ArticleRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _sourceItem = MutableStateFlow<UIState<List<Source>>>(UIState.Empty)
    val sourceItem: StateFlow<UIState<List<Source>>> = _sourceItem

    init {
        getSources()
    }

    fun getSources() {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                _sourceItem.emit(
                    UIState.Failure(
                        throwable = NoInternetException()
                    )
                )
                return@launch
            }
            _sourceItem.emit(UIState.Loading)
            newsRepository.getSources()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _sourceItem.emit(UIState.Failure(it))
                }
                .collect {
                    _sourceItem.emit(UIState.Success(it))
                }
        }
    }
}