package com.ritesh.newsreader.articles.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.ritesh.newsreader.articles.data.repository.database.entity.Source

@Keep
data class ApiArticle(

    @SerializedName("source")
    val source: Source,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("urlToImage")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?

)