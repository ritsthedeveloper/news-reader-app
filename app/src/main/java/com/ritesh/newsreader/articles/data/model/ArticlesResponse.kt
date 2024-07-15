package com.ritesh.newsreader.articles.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Main Article response wrapper.
 */
@Keep
data class ArticlesResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("articles")
    val articles: List<ApiArticle>
)