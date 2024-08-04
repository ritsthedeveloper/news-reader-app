package com.ritesh.newsreader.common.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.application.ArticlesUseCase
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.network.NoInternetException
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsPagingSourceV2 @Inject constructor(
    private val articlesUseCase: ArticlesUseCase,
    private val networkHelper: NetworkHelper,
    private val dispatcherProvider: DispatcherProvider
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        val page = params.key ?: 1
        lateinit var loadResult: LoadResult<Int, Article>

        withContext(dispatcherProvider.io) {
            kotlin.runCatching {
                checkPreCondition(page)
                val prevKey = calculatePrevKey(page)
                // Fetch from the Server using API call
                val articles = articlesUseCase.getNews(page)
                loadResult = LoadResult.Page(
                    data = articles,
                    prevKey = prevKey,
                    nextKey = if (articles.isEmpty()) null else page.plus(1)
                )
            }.onFailure {
                loadResult = LoadResult.Error(it)
            }
        }
        return loadResult
    }

    private fun checkPreCondition(page: Int) {

        if (!networkHelper.isNetworkConnected() && page != AppConstants.DEFAULT_PAGE_NUM) {
            // For following pages throw No internet exception
            throw NoInternetException()
        }
    }

    private fun calculatePrevKey(page: Int): Int? {
        return if (networkHelper.isNetworkConnected()) {
            if (page == 1) null else page.minus(1)
        } else {
            page.minus(1)
        }
    }
}