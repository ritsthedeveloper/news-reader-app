package com.ritesh.newsreader.articles.data.repository

import com.ritesh.newsreader.articles.data.model.ApiArticle
import com.ritesh.newsreader.articles.data.model.ArticlesResponse
import com.ritesh.newsreader.articles.data.repository.database.DatabaseService
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
import com.ritesh.newsreader.articles.data.repository.network.ApiInterface
import com.ritesh.newsreader.util.apiArticleListToArticleList
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

            val response = ArticlesResponse(
                status = "ok", totalResults = 1, articles = articles
            )

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
}