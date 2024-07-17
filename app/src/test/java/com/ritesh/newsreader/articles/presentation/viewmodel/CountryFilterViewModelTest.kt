package com.ritesh.newsreader.articles.presentation.viewmodel

import app.cash.turbine.test
import com.ritesh.newsreader.articles.data.model.Country
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
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
class CountryFilterViewModelTest {

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
    fun getCountries_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            Mockito
                .doReturn(flowOf(emptyList<Country>()))
                .`when`(articleRepository)
                .getCountries()

            val viewModel = getViewModelInstance()

            viewModel.countryItem.test {
                Assert.assertEquals(UIState.Success(emptyList<Country>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getCountries()
        }
    }

    @Test
    fun getCountries_whenRepositoryResponseError_shouldSetErrorUiState() {

        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Country>> {
                throw exception
            })
                .`when`(articleRepository)
                .getCountries()

            val viewModel = getViewModelInstance()

            viewModel.countryItem.test {
                Assert.assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getCountries()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getViewModelInstance(): CountryFilterViewModel {
        // SUT
        return CountryFilterViewModel(
            articleRepository,
            dispatcherProvider,
            networkHelper
        )
    }
}