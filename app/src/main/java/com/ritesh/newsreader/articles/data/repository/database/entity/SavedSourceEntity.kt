package com.ritesh.newsreader.articles.data.repository.database.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
@Keep
data class SavedSourceEntity(
    @ColumnInfo("sourceId")
    val id: String?,

    @ColumnInfo("sourceName")
    val name: String?
)