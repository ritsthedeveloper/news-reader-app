package com.ritesh.newsreader.articles.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ritesh.newsreader.articles.data.repository.database.entity.Article

@Composable
fun NewsArticleItem(article: Article, onItemClick: (Article) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(6.dp)
        .clickable {
            onItemClick(article)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(modifier = Modifier.height(150.dp)) {
            article.urlToImage?.let { ArticleImage(urlToImage = it, title = article.title) }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                article.title?.let { ArticleTitle(title = it) }
                article.description?.let { ArticleDescription(description = it) }
            }
        }
    }
}

@Composable
fun ArticleImage(urlToImage: String, title: String?) {
    AsyncImage(
        model = urlToImage,
        contentDescription = title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(150.dp)
    )
}

@Composable
fun ArticleTitle(title: String) {
    Text(
        text = title,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun ArticleDescription(description: String) {
    Text(
        text = description,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.titleSmall
    )
}