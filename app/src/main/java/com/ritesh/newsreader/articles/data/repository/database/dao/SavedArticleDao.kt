package com.ritesh.newsreader.articles.data.repository.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ritesh.newsreader.articles.data.repository.database.entity.SavedArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: SavedArticleEntity): Long //update or insert

    @Query("SELECT * FROM saved_articles")
    fun getSavedArticles(): Flow<List<SavedArticleEntity>>

    @Delete
    suspend fun deleteArticle(article: SavedArticleEntity)
}