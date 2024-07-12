package com.ritesh.newsreader.articles.data.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ritesh.newsreader.articles.data.repository.database.dao.ArticleDao
import com.ritesh.newsreader.articles.data.repository.database.dao.SavedArticleDao
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.data.repository.database.entity.SavedArticleEntity

@Database(entities = [SavedArticleEntity::class, Article::class], version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getSavedArticleDao(): SavedArticleDao
    abstract fun getArticleDao(): ArticleDao
}