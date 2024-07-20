package com.ritesh.newsreader.articles.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ritesh.newsreader.R
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.presentation.viewmodel.SharedViewModel
import com.ritesh.newsreader.common.ui.base.ShowError
import com.ritesh.newsreader.articles.presentation.components.NewsLayoutWithDelete

/**
 * Screen to display list of saved/bookmarked news-article.
 */
@Composable
fun SavedScreen(
    sharedViewModel: SharedViewModel = hiltViewModel(),
    newsClicked: (Article) -> Unit
) {

    val newsList: List<Article> by sharedViewModel.getSavedNews()
        .collectAsStateWithLifecycle(emptyList())

    if (newsList.isEmpty()) {
        ShowError(text = stringResource(R.string.no_saved_news))
    } else {
        NewsLayoutWithDelete(newsList = newsList,
            articleClicked = {
                // on clicking any saved article, navigate user to article detail screen
                newsClicked(it)
            }) {
            sharedViewModel.deleteArticle(it)
        }
    }

}