package com.ritesh.newsreader.articles.application

import app.cash.turbine.test
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.model.ApiArticle
import com.ritesh.newsreader.articles.data.model.ArticlesResponse
import com.ritesh.newsreader.articles.data.repository.ArticleRepositoryV2
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
import com.ritesh.newsreader.logger.Logger
import com.ritesh.newsreader.logger.TestLogger
import com.ritesh.newsreader.util.apiArticleListToArticleList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArticlesUseCaseTest {

    // SUT
    private lateinit var articlesUseCase: ArticlesUseCase

    @Mock
    private lateinit var articleRepository: ArticleRepositoryV2
    private lateinit var logger: Logger

    @Before
    fun setUp() {
        logger = TestLogger()
        /**
         * Before the actual test set up the Use-Case with Mocked Repo
         */
        articlesUseCase = ArticlesUseCase(articleRepository, logger)
    }

    @Test
    fun getNewsByCategory_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val response = getArticlesMockedSuccessResponse()
            Mockito
                .doReturn(response.articles.apiArticleListToArticleList())
                .`when`(articleRepository)
                .getNewsByCategoryV2(AppConstants.DEFAULT_CATEGORY)

            articlesUseCase
                .getNewsByCategory(AppConstants.DEFAULT_CATEGORY)
                .test {
                    Assert.assertEquals(response.articles.apiArticleListToArticleList(), awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
        }
    }

    private fun getArticlesMockedSuccessResponse(): ArticlesResponse {
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
}