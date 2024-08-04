package com.ritesh.newsreader.articles.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.runtime.Composable
import com.ritesh.newsreader.articles.data.repository.database.entity.Article

/**
 * Displays the list of news-articles along with a callback to listen to any article click
 * and passing the clicked article back.
 */
@Composable
fun NewsLayout(
    newsList: List<Article>,
    articleClicked: (Article) -> Unit
) {
    LazyColumn {
        items(newsList) {
            NewsArticleItem(it) { article ->
                articleClicked(article)
            }
        }
    }
}

/**
 * Displays the list of news-articles along with a callback to listen to any article click
 * and passing the clicked article back. It also provides delete functionality to remove articles
 * from Saved/Bookmarked news-articles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsLayoutWithDelete(
    newsList: List<Article>,
    articleClicked: (Article) -> Unit,
    deleteArticle: (Article) -> Unit
) {
    LazyColumn {
        items(newsList, key = { article -> article.url!! }) { article ->
            // remember the swipe dismiss state during compositions.
            val dismissState = rememberDismissState()
            // Delete the article from Saved/Bookmarked news-articles, if swipe to dismiss direction is
            // Right to left (End to start) or Left to right( Start to End)
            if (dismissState.isDismissed(direction = DismissDirection.EndToStart)
                || dismissState.isDismissed(direction = DismissDirection.StartToEnd)
            ) {
                // Remove the particular news-article view.
                deleteArticle(article)
            }
            // Swipe to dismiss composable helps in deleting the content when User swipes R to L or L to R.
            SwipeToDismiss(state = dismissState,
                background = {},
                dismissContent = {
                    // Send back the selected Article on clicking the Bookmarked news-article for further processing
                    // like opening the detail screen etc.
                    NewsArticleItem(article) {
                        articleClicked(it)
                    }
                })
        }
    }
}