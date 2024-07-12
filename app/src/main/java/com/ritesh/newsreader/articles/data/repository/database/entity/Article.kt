package com.ritesh.newsreader.articles.data.repository.database.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cached_articles",
    indices = [Index(
        value = ["title", "url"],
        unique = true
    )]
)
@Keep
data class Article(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Int = 0,

    @Embedded
    val source: Source,

    @ColumnInfo("author")
    val author: String?,

    @ColumnInfo("title")
    val title: String?,

    @ColumnInfo("description")
    val description: String?,

    @ColumnInfo("url")
    val url: String?,

    @ColumnInfo("urlToImage")
    val urlToImage: String?,

    @ColumnInfo("publishedAt")
    val publishedAt: String?,

    @ColumnInfo("content")
    val content: String?

)