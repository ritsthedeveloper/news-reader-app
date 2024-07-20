package com.ritesh.newsreader.articles.data.repository.database.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import com.ritesh.newsreader.articles.data.model.NewsFilter

@Keep
data class Source(
    @ColumnInfo("sourceId")
    val id: String?,

    @ColumnInfo("sourceName")
    val name: String?
) : NewsFilter