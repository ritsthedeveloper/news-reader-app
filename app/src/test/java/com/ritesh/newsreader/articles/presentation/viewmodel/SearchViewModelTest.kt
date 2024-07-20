package com.ritesh.newsreader.articles.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.dispatcher.TestDispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.newtork.TestNetworkHelper
import com.ritesh.newsreader.common.ui.base.UIState
import com.ritesh.newsreader.logger.TestLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkHelper: NetworkHelper

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        Dispatchers.setMain(dispatcherProvider.main)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun searchNews_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
//        runTest {
//            Mockito
//                .doReturn(flowOf(emptyList<Article>()))
//                .`when`(articleRepository)
//                .searchNews("Narendra Modi")
//
//            val viewModel = getViewModelInstance()
//            viewModel.searchNewsItem.test {
//                 Assert.assertEquals(UIState.Success(emptyList<Article>()), awaitItem())
//                cancelAndIgnoreRemainingEvents()
//            }
//
//            Mockito
//                .verify(articleRepository, Mockito.times(1))
//                .searchNews("Narendra Modi")
//        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getViewModelInstance() : SearchViewModel{
        // SUT
        return SearchViewModel(
            articleRepository,
            dispatcherProvider,
            networkHelper
        )
    }
}