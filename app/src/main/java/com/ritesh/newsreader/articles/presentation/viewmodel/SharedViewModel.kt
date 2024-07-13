package com.ritesh.newsreader.articles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch(dispatcherProvider.io) {
        articleRepository.upsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch(dispatcherProvider.io) {
        articleRepository.deleteArticle(article)
    }

    fun getSavedNews() = articleRepository.getSavedNews()
}