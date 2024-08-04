package com.ritesh.newsreader.articles.data.repository

import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.repository.database.DatabaseService
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.articles.data.repository.network.ApiInterface
import com.ritesh.newsreader.util.apiArticleListToArticleList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ArticleRepositoryV2 @Inject constructor(
    // Local Data Source
    private val database: DatabaseService,
    // Remote Data Source
    private val network: ApiInterface
) {
    /****************************** V2 methods for Usecase approach **************/

    suspend fun getNews(pageNumber: Int = AppConstants.DEFAULT_PAGE_NUM): List<Article> {
        val articles = network.getNews(
            pageNum = pageNumber
        ).articles.apiArticleListToArticleList()

        return if (pageNumber == AppConstants.DEFAULT_PAGE_NUM) {
            database.deleteAllAndInsertAll(articles)
            database.getAllArticles().first()
        } else {
            articles
        }
    }

    suspend fun getNewsByCountryV2(
        countryCode: String,
        pageNumber: Int = AppConstants.DEFAULT_PAGE_NUM
    ): List<Article> {
        return network.getNews(
            countryCode,
            pageNum = pageNumber
        ).articles.apiArticleListToArticleList()
    }

    suspend fun getNewsByCategoryV2(
        categoryCode: String,
        pageNumber: Int = AppConstants.DEFAULT_PAGE_NUM
    ): List<Article> {

        return network.getNewsByCategory(
            categoryCode,
            pageNum = pageNumber
        ).articles.apiArticleListToArticleList()
    }

    suspend fun getNewsByLanguageV2(
        languageCode: String,
        pageNumber: Int = AppConstants.DEFAULT_PAGE_NUM
    ): List<Article> {
        return network.getNewsByLang(
            languageCode,
            pageNum = pageNumber
        ).articles.apiArticleListToArticleList()
    }

    suspend fun getNewsBySourceV2(
        sourceCode: String,
        pageNumber: Int = AppConstants.DEFAULT_PAGE_NUM
    ): List<Article> {

        return network.getNewsBySource(
            sourceCode,
            pageNum = pageNumber
        ).articles.apiArticleListToArticleList()
    }
}