package com.ritesh.newsreader.articles.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ritesh.newsreader.R
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.presentation.viewmodel.SearchViewModel
import com.ritesh.newsreader.common.network.NoInternetException
import com.ritesh.newsreader.common.ui.base.ShowError
import com.ritesh.newsreader.common.ui.base.ShowLoading
import com.ritesh.newsreader.common.ui.base.UIState
import com.ritesh.newsreader.articles.presentation.components.NewsLayout
import com.ritesh.newsreader.util.filterArticles

/**
 * Screen provide UI to search the article by providing the info about article in the search bar provided.
 */
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    backPressed: () -> Unit,
    newsClicked: (Article) -> Unit
) {
    val searchUIState: UIState<List<Article>> by searchViewModel.searchNewsItem.collectAsStateWithLifecycle()
    val searchQuery: String by searchViewModel.query.collectAsStateWithLifecycle()
    SearchLayout(
        searchQuery = searchQuery,
        searchUIState = searchUIState,
        newsClicked = newsClicked,
        retrySearch = {
            searchViewModel.searchNews()
        },
        backPressed = backPressed
    ) {
        searchViewModel.searchNews(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLayout(
    searchQuery: String,
    searchUIState: UIState<List<Article>>,
    newsClicked: (Article) -> Unit,
    retrySearch: () -> Unit,
    backPressed: () -> Unit,
    onSearchQueryChange: (String) -> Unit
) {

    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = {},
        placeholder = {
            Text(text = stringResource(id = R.string.search))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        active = true,
        onActiveChange = {},
        tonalElevation = 0.dp
    ) {
        when (searchUIState) {
            is UIState.Loading -> {
                ShowLoading()
            }

            is UIState.Failure -> {
                var errorText = stringResource(id = R.string.something_went_wrong)
                if (searchUIState.throwable is NoInternetException) {
                    errorText = stringResource(id = R.string.no_internet_available)
                }
                ShowError(
                    text = errorText,
                    retryEnabled = true
                ) {
                    retrySearch()
                }
            }

            is UIState.Success -> {
                if (searchUIState.data.filterArticles().isEmpty()) {
                    ShowError(text = stringResource(id = R.string.no_data_available))
                } else {
                    NewsLayout(newsList = searchUIState.data.filterArticles()) {
                        newsClicked(it)
                    }
                }
            }

            is UIState.Empty -> {

            }
        }
    }

    BackHandler {
        backPressed()
    }
}