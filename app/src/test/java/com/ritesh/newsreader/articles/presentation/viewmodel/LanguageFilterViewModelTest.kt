package com.ritesh.newsreader.articles.presentation.viewmodel

import app.cash.turbine.test
import com.ritesh.newsreader.articles.data.model.Language
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.dispatcher.TestDispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.newtork.TestNetworkHelper
import com.ritesh.newsreader.common.ui.base.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class LanguageFilterViewModelTest {

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

    @Test
    fun getLanguages_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            Mockito
                .doReturn(flowOf(emptyList<Language>()))
                .`when`(articleRepository)
                .getLanguages()

            val viewModel = getViewModelInstance()

            viewModel.languageItem.test {
                Assert.assertEquals(UIState.Success(emptyList<Language>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getLanguages()
        }
    }

    @Test
    fun getLanguages_whenRepositoryResponseError_shouldSetErrorUiState() {

        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Language>> {
                throw exception
            })
                .`when`(articleRepository)
                .getLanguages()

            val viewModel = getViewModelInstance()

            viewModel.languageItem.test {
                Assert.assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getLanguages()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getViewModelInstance(): LanguageFilterViewModel {
        // SUT
        return LanguageFilterViewModel(
            articleRepository,
            dispatcherProvider,
            networkHelper
        )
    }
}