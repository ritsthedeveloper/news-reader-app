package com.ritesh.newsreader.articles.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import app.cash.turbine.test
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.application.ArticlesUseCase
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.dispatcher.TestDispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.newtork.TestNetworkHelper
import com.ritesh.newsreader.common.ui.base.UIState
import com.ritesh.newsreader.logger.Logger
import com.ritesh.newsreader.logger.TestLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
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
class NewsViewModelV2Test {

    @Mock
    private lateinit var articlesUseCase: ArticlesUseCase

    @Mock
    private lateinit var newsPagingSource: Pager<Int, Article>

    private lateinit var logger: Logger
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkHelper: NetworkHelper

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        logger = TestLogger()
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        Dispatchers.setMain(dispatcherProvider.main)
    }

    @Test
    fun fetchNewsByCountry_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {

            Mockito
                .doReturn(flowOf(emptyList<Article>()))
                .`when`(articlesUseCase)
                .getNewsByCountry(AppConstants.DEFAULT_COUNTRY)

            val viewModel = getViewModelInstance(getCountrySavedStateHandle())

            viewModel.newsItem.test {
                Assert.assertEquals(UIState.Success(emptyList<Article>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articlesUseCase, Mockito.times(1))
                .getNewsByCountry(AppConstants.DEFAULT_COUNTRY)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getCountrySavedStateHandle() : SavedStateHandle {
        return SavedStateHandle().apply {
            set("country", AppConstants.DEFAULT_COUNTRY)
        }
    }

    private fun getViewModelInstance(savedStateHandle : SavedStateHandle) : NewsViewModelV2{
        // SUT
        return NewsViewModelV2(
            articlesUseCase,
            savedStateHandle,
            newsPagingSource,
            networkHelper,
            dispatcherProvider,
            logger
        )
    }
}