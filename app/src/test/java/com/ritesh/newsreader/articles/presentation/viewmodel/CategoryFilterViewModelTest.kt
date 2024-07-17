package com.ritesh.newsreader.articles.presentation.viewmodel

import app.cash.turbine.test
import com.ritesh.newsreader.articles.data.model.Category
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
class CategoryFilterViewModelTest {
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
    fun getCategories_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            Mockito
                .doReturn(flowOf(emptyList<Category>()))
                .`when`(articleRepository)
                .getCategories()

            val viewModel = getViewModelInstance()

            viewModel.categoryItem.test {
                Assert.assertEquals(UIState.Success(emptyList<Category>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getCategories()
        }
    }

    @Test
    fun getCategories_whenRepositoryResponseError_shouldSetErrorUiState() {

        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Category>> {
                throw exception
            })
                .`when`(articleRepository)
                .getCategories()

            val viewModel = getViewModelInstance()

            viewModel.categoryItem.test {
                Assert.assertEquals(
                    UIState.Failure(exception, null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getCategories()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getViewModelInstance(): CategoryFilterViewModel {
        // SUT
        return CategoryFilterViewModel(
            articleRepository,
            dispatcherProvider,
            networkHelper
        )
    }
}