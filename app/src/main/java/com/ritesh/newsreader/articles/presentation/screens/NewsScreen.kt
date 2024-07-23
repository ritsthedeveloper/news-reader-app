package com.ritesh.newsreader.articles.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ritesh.newsreader.R
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.presentation.viewmodel.NewsViewModel
import com.ritesh.newsreader.common.network.NoInternetException
import com.ritesh.newsreader.common.ui.base.ShowError
import com.ritesh.newsreader.common.ui.base.ShowLoading
import com.ritesh.newsreader.common.ui.base.UIState
import com.ritesh.newsreader.articles.presentation.components.NewsArticleItem
import com.ritesh.newsreader.articles.presentation.components.NewsLayout
import com.ritesh.newsreader.articles.presentation.viewmodel.NewsViewModelV2
import com.ritesh.newsreader.util.filterArticles

/**
 * Screen to display News-list
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsScreen(
//    newsViewModel: NewsViewModel = hiltViewModel(),
    newsViewModel: NewsViewModelV2 = hiltViewModel(),
    newsClicked: (Article) -> Unit
) {
    newsViewModel.logger.d("NewsScreen", "Inside NewsScreen")

    val newsUiState: UIState<List<Article>> by newsViewModel.newsItem.collectAsStateWithLifecycle()

    // Refreshing state will automatically be saved and restored during configuration changes,
    // ensuring that the UI retains its state.
    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    // Helps remembering the PullRefresh State across recompositions.
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            // set the refreshing status true on triggering the refresh request
            isRefreshing = true
            // start fetching the news making new request.
            newsViewModel.fetchNews()
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        when (newsUiState) {
            is UIState.Loading -> {
                if (!isRefreshing)
                    ShowLoading() // Display loader while loading the content first time and not during refresh request
            }

            is UIState.Failure -> {
                isRefreshing = false
                var errorText = stringResource(id = R.string.something_went_wrong)
                if ((newsUiState as UIState.Failure<List<Article>>).throwable is NoInternetException) {
                    errorText = stringResource(id = R.string.no_internet_available)
                }
                // Display Error view with retry option enabled for better UX.
                ShowError(
                    text = errorText,
                    retryEnabled = true
                ) {
                    // Request for Fetching the news again on clicking the retry button.
                    newsViewModel.fetchNews()
                }
            }

            is UIState.Success -> {
                isRefreshing = false
                if ((newsUiState as UIState.Success<List<Article>>).data.filterArticles()
                        .isEmpty()
                ) {
                    // Show no data available error if list fetched is empty.
                    ShowError(text = stringResource(R.string.no_data_available))
                } else {
                    NewsLayout(newsList = (newsUiState as UIState.Success<List<Article>>).data.filterArticles()) {
                        newsClicked(it)
                    }
                }
            }

            is UIState.Empty -> {

            }
        }

        // Pull to refresh composable providing the Pull to refresh behavior.
        PullRefreshIndicator(
            refreshing = false /*isRefreshing*/,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsScreenPaging(
//        newsViewModel: NewsViewModel = hiltViewModel(),
    newsViewModel: NewsViewModelV2 = hiltViewModel(),
    newsClicked: (Article) -> Unit
) {
    newsViewModel.logger.d("NewsScreen", "Inside NewsScreenPaging")
    // This extension helps in collecting values from this Flow of PagingData and represents them inside a
    // LazyPagingItems instance.
    val pagingResponse = newsViewModel.newsItemPaging.collectAsLazyPagingItems()

    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            newsViewModel.fetchNews()
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        when (pagingResponse.loadState.refresh) {
            is LoadState.Loading -> {
                if (!isRefreshing)
                    ShowLoading()
            }

            is LoadState.Error -> {
                isRefreshing = false
                var errorText = stringResource(id = R.string.something_went_wrong)
                if ((pagingResponse.loadState.refresh as LoadState.Error).error is NoInternetException) {
                    errorText = stringResource(id = R.string.no_internet_available)
                }
                ShowError(
                    text = errorText,
                    retryEnabled = true
                ) {
                    newsViewModel.fetchNews()
                }
            }

            else -> {
                isRefreshing = false
                NewsPagingAppend(pagingResponse, newsClicked)
            }
        }

        PullRefreshIndicator(
            refreshing = false /*isRefreshing*/,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun NewsPagingAppend(
    pagingResponse: LazyPagingItems<Article>,
    newsClicked: (Article) -> Unit,
) {
    LazyColumn {
        items(pagingResponse.itemCount) {
            if (pagingResponse[it] != null) {
                NewsArticleItem(pagingResponse[it]!!) { article ->
                    newsClicked(article)
                }
            }
        }
        pagingResponse.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.align(Alignment.Center),
//                                strokeWidth = 1.dp
//                            )
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        ShowError(
                            text = stringResource(id = R.string.retry_on_pagination),
                            retryEnabled = true
                        ) {
                            pagingResponse.retry()
                        }
                    }
                }

                else -> {}
            }
        }
    }
}