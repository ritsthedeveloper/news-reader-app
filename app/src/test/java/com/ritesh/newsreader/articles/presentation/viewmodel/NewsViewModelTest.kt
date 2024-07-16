package com.ritesh.newsreader.articles.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import app.cash.turbine.test
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.dispatcher.TestDispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.newtork.TestNetworkHelper
import com.ritesh.newsreader.common.ui.base.UIState
import com.ritesh.newsreader.logger.Logger
import com.ritesh.newsreader.logger.TestLogger
import org.junit.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {
    @Mock
    private lateinit var articleRepository: ArticleRepository
    @Mock
    private lateinit var newsPagingSource: Pager<Int, Article>

    private lateinit var logger: Logger
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkHelper: NetworkHelper

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
                .`when`(articleRepository)
                .getNewsByCountry(AppConstants.DEFAULT_COUNTRY)

            val viewModel = getViewModelInstance(getCountrySavedStateHandle())

            viewModel.newsItem.test {
                assertEquals(UIState.Success(emptyList<Article>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getNewsByCountry(AppConstants.DEFAULT_COUNTRY)
        }
    }

    @Test
    fun fetchNewsByCountry_whenRepositoryResponseError_shouldSetErrorUiState() {

        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Article>> {
                throw exception
            })
                .`when`(articleRepository)
                .getNewsByCountry(AppConstants.DEFAULT_COUNTRY)

            val viewModel = getViewModelInstance(getCountrySavedStateHandle())

            viewModel.newsItem.test {
                assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getNewsByCountry(AppConstants.DEFAULT_COUNTRY)
        }
    }

    @Test
    fun fetchNewsByCategory_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {

        runTest {
            Mockito
                .doReturn(flowOf(emptyList<Article>()))
                .`when`(articleRepository)
                .getNewsByCategory(AppConstants.DEFAULT_CATEGORY)

            val viewModel = getViewModelInstance(getCategorySavedStateHandle())

            viewModel.newsItem.test {
                assertEquals(UIState.Success(emptyList<Article>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getNewsByCategory(AppConstants.DEFAULT_CATEGORY)
        }
    }

    @Test
    fun fetchNewsByCategory_whenRepositoryResponseError_shouldSetErrorUiState() {

        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Article>> {
                throw exception
            })
                .`when`(articleRepository)
                .getNewsByCategory(AppConstants.DEFAULT_CATEGORY)

            val viewModel = getViewModelInstance(getCategorySavedStateHandle())

            viewModel.newsItem.test {
                assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getNewsByCategory(AppConstants.DEFAULT_CATEGORY)
        }
    }

    @Test
    fun fetchNewsByLanguage_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            Mockito
                .doReturn(flowOf(emptyList<Article>()))
                .`when`(articleRepository)
                .getNewsByLanguage(AppConstants.DEFAULT_LANGUAGE)

            val viewModel = getViewModelInstance(getLanguageSavedStateHandle())

            viewModel.newsItem.test {
                assertEquals(UIState.Success(emptyList<Article>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getNewsByLanguage(AppConstants.DEFAULT_LANGUAGE)
        }
    }

    @Test
    fun fetchNewsByLanguage_whenRepositoryResponseError_shouldSetErrorUiState(){
        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Article>> {
                throw exception
            })
                .`when`(articleRepository)
                .getNewsByLanguage(AppConstants.DEFAULT_LANGUAGE)

            val viewModel = getViewModelInstance(getLanguageSavedStateHandle())

            viewModel.newsItem.test {
                assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getNewsByLanguage(AppConstants.DEFAULT_LANGUAGE)
        }
    }

    @Test
    fun fetchNewsBySource_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {

        runTest {
            Mockito
                .doReturn(flowOf(emptyList<Article>()))
                .`when`(articleRepository)
                .getNewsBySource(AppConstants.DEFAULT_SOURCE)

            val viewModel = getViewModelInstance(getSourceSavedStateHandle())

            viewModel.newsItem.test {
                assertEquals(UIState.Success(emptyList<Article>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getNewsBySource(AppConstants.DEFAULT_SOURCE)
        }
    }
    @Test
    fun fetchNewsBySource_whenRepositoryResponseError_shouldSetErrorUiState(){
        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Article>> {
                throw exception
            })
                .`when`(articleRepository)
                .getNewsBySource(AppConstants.DEFAULT_SOURCE)

            val viewModel = getViewModelInstance(getSourceSavedStateHandle())

            viewModel.newsItem.test {
                assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getNewsBySource(AppConstants.DEFAULT_SOURCE)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getCountrySavedStateHandle() : SavedStateHandle {
        return SavedStateHandle().apply {
            set("country", AppConstants.DEFAULT_COUNTRY)
        }
    }

    private fun getCategorySavedStateHandle() : SavedStateHandle {
        return SavedStateHandle().apply {
            set("category", AppConstants.DEFAULT_CATEGORY)
        }
    }
    private fun getLanguageSavedStateHandle() : SavedStateHandle {
        return SavedStateHandle().apply {
            set("language", AppConstants.DEFAULT_LANGUAGE)
        }
    }

    private fun getSourceSavedStateHandle() : SavedStateHandle {
        return SavedStateHandle().apply {
            set("source", AppConstants.DEFAULT_SOURCE)
        }
    }

    private fun getViewModelInstance(savedStateHandle : SavedStateHandle) : NewsViewModel{
        // SUT
        return NewsViewModel(
            savedStateHandle,
            articleRepository,
            newsPagingSource,
            logger,
            dispatcherProvider,
            networkHelper
        )
    }
}