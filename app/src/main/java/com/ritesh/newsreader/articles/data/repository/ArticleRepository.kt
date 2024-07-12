package com.ritesh.newsreader.articles.data.repository

import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.AppConstants.DEFAULT_PAGE_NUM
import com.ritesh.newsreader.articles.data.model.Country
import com.ritesh.newsreader.articles.data.model.Language
import com.ritesh.newsreader.articles.data.repository.database.DatabaseService
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.data.repository.database.entity.Source
import com.ritesh.newsreader.articles.data.repository.network.ApiInterface
import com.ritesh.newsreader.util.apiArticleListToArticleList
import com.ritesh.newsreader.util.apiSourceListToSourceList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val database: DatabaseService,
    private val network: ApiInterface
) {

    suspend fun getNews(pageNumber: Int = DEFAULT_PAGE_NUM): List<Article> {
        val articles = network.getNews(
            pageNum = pageNumber
        ).articles.apiArticleListToArticleList()
        return if (pageNumber == DEFAULT_PAGE_NUM) {
            database.deleteAllAndInsertAll(articles)
            database.getAllArticles().first()
        } else {
            articles
        }
    }

    suspend fun getNewsByCountry(
        countryCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ): Flow<List<Article>> = flow {
        emit(
            network.getNews(
                countryCode,
                pageNum = pageNumber
            ).articles.apiArticleListToArticleList()
        )
    }

    suspend fun getNewsByLanguage(
        languageCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ): Flow<List<Article>> = flow {
        emit(
            network.getNewsByLang(
                languageCode,
                pageNum = pageNumber
            ).articles.apiArticleListToArticleList()
        )
    }

    suspend fun getNewsBySource(
        sourceCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ): Flow<List<Article>> = flow {
        emit(
            network.getNewsBySource(
                sourceCode,
                pageNum = pageNumber
            ).articles.apiArticleListToArticleList()
        )
    }

    suspend fun searchNews(
        searchQuery: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ): Flow<List<Article>> =
        flow {
            emit(
                network.searchNews(searchQuery, pageNumber).articles.apiArticleListToArticleList()
            )
        }

    suspend fun upsert(article: Article) {
        database.upsert(article)
    }

    suspend fun deleteArticle(article: Article) = database.deleteArticle(article)

    // Get BookMarked News Articles
    fun getSavedNews(): Flow<List<Article>> = database.getSavedArticles()

    suspend fun getSources(): Flow<List<Source>> = flow {
        emit(
            network.getSources().sources.apiSourceListToSourceList()
        )
    }
    suspend fun getCountries(): Flow<List<Country>> = flow {
        emit(AppConstants.countryList)
    }

    suspend fun getLanguages(): Flow<List<Language>> = flow {
        emit(AppConstants.languageList)
    }
}