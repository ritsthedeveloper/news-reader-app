package com.ritesh.newsreader.articles.presentation.viewmodel

import app.cash.turbine.test
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.dispatcher.TestDispatcherProvider
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
class SharedViewModelTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        Dispatchers.setMain(dispatcherProvider.main)
    }

    @Test
    fun getSavedNews_whenRepositoryResponseSuccess_shouldGiveResult() {
        runTest {
            Mockito
//                .doReturn(flowOf(emptyList<Article>()))
                .doReturn(flowOf(getSavedArticlesMockedSuccessResponse()))
                .`when`(articleRepository)
                .getSavedNews()

            val viewModel = getViewModelInstance()

            viewModel.getSavedNews().test {
//                Assert.assertEquals(emptyList<Article>(), awaitItem())
                Assert.assertEquals(getSavedArticlesMockedSuccessResponse(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getSavedNews()
        }
    }

    @Test
    fun getSavedNews_whenRepositoryResponseError_shouldGiveError() {
        runTest {
            val errorMessage = "Error Message"
            val exception = IllegalStateException(errorMessage)

            Mockito.doReturn(flow<List<Article>> {
                throw exception
            })
                .`when`(articleRepository)
                .getSavedNews()

            val viewModel = getViewModelInstance()

            viewModel.getSavedNews().test {
                Assert.assertEquals(
                    exception.toString(),
                    awaitError().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }

            Mockito
                .verify(articleRepository, Mockito.times(1))
                .getSavedNews()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getViewModelInstance(): SharedViewModel {
        // SUT
        return SharedViewModel(
            articleRepository,
            dispatcherProvider
        )
    }

    private fun getSavedArticlesMockedSuccessResponse(): List<Article> {
        // Success response mocked
        val source = Source(id = "sourceId", name = "sourceName")
        val article = Article(
            id = 1,
            source = source,
            title = "title",
            description = "description",
            url = "url",
            urlToImage = "urlToImage",
            author = "author",
            content = "content",
            publishedAt = "pat"
        )
        val articles = mutableListOf<Article>()
        articles.add(article)
        return articles
    }
}