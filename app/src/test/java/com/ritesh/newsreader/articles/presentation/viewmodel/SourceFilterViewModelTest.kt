package com.ritesh.newsreader.articles.presentation.viewmodel

import app.cash.turbine.test
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.dispatcher.TestDispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.newtork.TestNetworkHelper
import com.ritesh.newsreader.common.ui.base.UIState
import kotlinx.coroutines.Dispatchers
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
class SourceFilterViewModelTest {
    @Mock
    private lateinit var articleRepository: ArticleRepository
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkHelper: NetworkHelper

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        Dispatchers.setMain(dispatcherProvider.main)
    }

    @Test
    fun getSources_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            Mockito
                .doReturn(flowOf(emptyList<Source>()))
                .`when`(articleRepository)
                .getSources()

            val viewModel = getViewModelInstance()

            viewModel.sourceItem.test {
                Assert.assertEquals(UIState.Success(emptyList<Source>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getSources()
        }
    }

    @Test
    fun getSources_whenRepositoryResponseError_shouldSetErrorUiState() {

        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Source>> {
                throw exception
            })
                .`when`(articleRepository)
                .getSources()

            val viewModel = getViewModelInstance()

            viewModel.sourceItem.test {
                Assert.assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getSources()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getViewModelInstance(): SourceFilterViewModel {
        // SUT
        return SourceFilterViewModel(
            articleRepository,
            dispatcherProvider,
            networkHelper
        )
    }

}