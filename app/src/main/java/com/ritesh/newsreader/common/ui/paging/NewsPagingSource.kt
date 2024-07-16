package com.ritesh.newsreader.common.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.repository.ArticleRepository
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.common.dispatcher.DispatcherProvider
import com.ritesh.newsreader.common.network.NetworkHelper
import com.ritesh.newsreader.common.network.NoInternetException
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class provides the implementation of fetching the news-articles from data sources like local DB
 * and server using API with paging.
 */
@Singleton
class NewsPagingSource @Inject constructor(
    private val newsRepository: ArticleRepository,
    private val networkHelper: NetworkHelper,
    private val dispatcherProvider: DispatcherProvider
) : PagingSource<Int, Article>() {

    /**
     * It returns the key to pass into the load() method when the data is refreshed or invalidated
     * after the initial load. The Paging Library calls this method automatically on subsequent
     * refreshes of the data.
     *
     * Finding the page key of the closest page to anchorPosition from either the prevKey or the nextKey;     *
     * prevKey == null -> anchorPage is the first page.
     * nextKey == null -> anchorPage is the last page.
     * both prevKey and nextKey are null -> anchorPage is the
     * initial page, so return null.
     */

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
//        return state.anchorPosition
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    /**
     * Retrieve paged data from the corresponding data source.
     *
     * The LoadParams object contains information about the load operation to be performed.
     * This includes the key to be loaded and the number of items to be loaded.
     *
     * LoadResult is a sealed class with two forms
     * Success - LoadResult.Page
     * Error - LoadResult.Error
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        val page = params.key ?: 1
        lateinit var loadResult: LoadResult<Int, Article>

        withContext(dispatcherProvider.io) {
            kotlin.runCatching {

                if (networkHelper.isNetworkConnected()) {
                    // Fetch from the Server using API call
                    val articles = newsRepository.getNews(page)
                    loadResult = LoadResult.Page(
                        data = articles,
                        prevKey = if (page == 1) null else page.minus(1),
                        nextKey = if (articles.isEmpty()) null else page.plus(1)
                    )
                }else{
                    // In case of no n/w, fetch the first page from the Local DB.
                    if (page == AppConstants.DEFAULT_PAGE_NUM) {
                        val articles = newsRepository.getNewsFromDb()
                        loadResult = LoadResult.Page(
                            data = articles,
                            prevKey = page.minus(1),
                            nextKey = if (articles.isEmpty()) null else page.plus(1)
                        )
                    } else {
                        // For following pages throw No internet exception
                        throw NoInternetException()
                    }
                }

//                if (!networkHelper.isNetworkConnected()) {
//                    if (page == AppConstants.DEFAULT_PAGE_NUM) {
//                        val articles = newsRepository.getNewsFromDb()
//                        loadResult = LoadResult.Page(
//                            data = articles,
//                            prevKey = page.minus(1),
//                            nextKey = if (articles.isEmpty()) null else page.plus(1)
//                        )
//                    } else {
//                        throw NoInternetException()
//                    }
//                } else {
//                    val articles = newsRepository.getNews(page)
//                    loadResult = LoadResult.Page(
//                        data = articles,
//                        prevKey = if (page == 1) null else page.minus(1),
//                        nextKey = if (articles.isEmpty()) null else page.plus(1)
//                    )
//                }
            }.onFailure {
                loadResult = LoadResult.Error(it)
            }
        }
        return loadResult
    }
}