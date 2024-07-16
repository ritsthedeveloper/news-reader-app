package com.ritesh.newsreader.common.ui.paging

import androidx.paging.PagingSource
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.dispatcher.TestDispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.newtork.TestNetworkHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsPagingSourceTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository

    private lateinit var pagingSource: NewsPagingSource
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkHelper: NetworkHelper

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        pagingSource = NewsPagingSource(articleRepository, networkHelper, dispatcherProvider)
    }

    @Test
    fun load_whenParamCorrect_shouldGiveResult() {

        runTest {
            // Given
            val page = 1
            val articles = emptyList<Article>()

            Mockito.doReturn(articles)
                .`when`(articleRepository)
                .getNews(page)

            // When
            val result = pagingSource.load(PagingSource.LoadParams.Refresh(page, 1, true))

            // Then
            val expected = PagingSource.LoadResult.Page(
                data = articles,
                prevKey = null,
                nextKey = null
            )

            assertEquals(expected, result)
        }
    }

    @Test
    fun load_whenResponseFailed_shouldGiveError() {

        runTest {
            // Given
            val page = 1
            val error = RuntimeException("Fake error")
            Mockito.doThrow(error)
                .`when`(articleRepository)
                .getNews(page)

            // When
            val result = pagingSource.load(PagingSource.LoadParams.Refresh(page, 1, false))

            // Then
            val expected = PagingSource.LoadResult.Error<Int, Article>(error)
            assertEquals(expected.toString(), result.toString())
        }
    }
}