package com.ritesh.newsreader.articles.data.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(articles: List<Article>)

    @Query("SELECT * FROM cached_articles")
    fun getAllArticles(): Flow<List<Article>>

    @Query("DELETE FROM cached_articles")
    fun deleteAll()

    @Transaction
    fun deleteAllAndInsertAll(articles: List<Article>) {
        deleteAll()
        return insertAll(articles)
    }
}