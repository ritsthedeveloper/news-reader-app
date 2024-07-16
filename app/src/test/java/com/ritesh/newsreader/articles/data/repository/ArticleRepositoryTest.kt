package com.ritesh.newsreader.articles.data.repository

import app.cash.turbine.test
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.model.ApiArticle
import com.ritesh.newsreader.articles.data.model.ArticlesResponse
import com.ritesh.newsreader.articles.data.repository.database.DatabaseService
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
import com.ritesh.newsreader.articles.data.repository.network.ApiInterface
import com.ritesh.newsreader.common.ui.base.UIState
import com.ritesh.newsreader.sources.data.model.ApiSource
import com.ritesh.newsreader.sources.data.model.SourcesResponse
import com.ritesh.newsreader.util.apiArticleListToArticleList
import com.ritesh.newsreader.util.apiSourceListToSourceList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArticleRepositoryTest {

    // SUT
    private lateinit var articleRepository: ArticleRepository
    @Mock
    private lateinit var apiInterface: ApiInterface
    @Mock
    private lateinit var database: DatabaseService
    @Before
    fun setUp() {
        /**
         * Before the actual test set up the Repository with Mocked DB and API Instances.
         */
        articleRepository = ArticleRepository(database, apiInterface)
    }

    @Test
    fun getNews_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {

            val response = getMockedSuccessResponse()

            Mockito.doReturn(response).`when`(apiInterface).getNews()
            Mockito.doReturn(
                flowOf(
                    response.articles.apiArticleListToArticleList()
                )
            )
                .`when`(database).getAllArticles()

            val actual = articleRepository.getNews()
            assertEquals(response.articles.apiArticleListToArticleList(), actual)
        }
    }

    @Test
    fun getNews_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val errorMessage = "Error Message"

            Mockito.doThrow(RuntimeException(errorMessage)).`when`(apiInterface).getNews()

            Assert.assertThrows(RuntimeException::class.java) {
                runBlocking {
                    articleRepository.getNews()
                }
            }
        }
    }

    @Test
    fun getNewsByLanguage_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val response = getMockedSuccessResponse()
            Mockito.doReturn(response).`when`(apiInterface).getNewsByLang()

            articleRepository.getNewsByLanguage(AppConstants.DEFAULT_LANGUAGE).test {
                assertEquals(response.articles.apiArticleListToArticleList(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun getNewsBySource_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val response = getMockedSuccessResponse()
            Mockito.doReturn(response).`when`(apiInterface).getNewsBySource()

            articleRepository.getNewsBySource(AppConstants.DEFAULT_SOURCE).test {
                assertEquals(response.articles.apiArticleListToArticleList(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun getNewsByCategory_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val response = getMockedSuccessResponse()
            Mockito.doReturn(response).`when`(apiInterface).getNewsByCategory()

            articleRepository.getNewsByCategory(AppConstants.DEFAULT_CATEGORY).test {
                assertEquals(response.articles.apiArticleListToArticleList(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun searchNews_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val response = getMockedSuccessResponse()
            Mockito.doReturn(response).`when`(apiInterface).searchNews("India world cup")

            articleRepository.searchNews("India world cup").test {
                assertEquals(response.articles.apiArticleListToArticleList(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun getSources_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val response = getSourcesMockedSuccessResponse()
            Mockito.doReturn(response).`when`(apiInterface).getSources()

            articleRepository.getSources().test {
                assertEquals(response.sources.apiSourceListToSourceList(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    private fun getMockedSuccessResponse() : ArticlesResponse{
        // Success response mocked
        val source = Source(id = "sourceId", name = "sourceName")
        val article = ApiArticle(
            source = source,
            title = "title",
            description = "description",
            url = "url",
            urlToImage = "urlToImage",
            author = "author",
            content = "content",
            publishedAt = "pat"
        )
        val articles = mutableListOf<ApiArticle>()
        articles.add(article)
        return ArticlesResponse(
            status = "ok", totalResults = 1, articles = articles
        )
    }

    private fun getSourcesMockedSuccessResponse() : SourcesResponse{
        // Success response for Sources mocked
        val source = ApiSource(id = "sourceId", name = "sourceName")
        val sources = mutableListOf<ApiSource>()
        sources.add(source)

        return SourcesResponse(status = "ok", sources = sources)
    }
}