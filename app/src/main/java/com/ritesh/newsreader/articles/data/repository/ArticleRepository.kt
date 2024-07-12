package com.ritesh.newsreader.articles.data.repository

import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.AppConstants.DEFAULT_PAGE_NUM
import com.ritesh.newsreader.articles.data.model.Country
import com.ritesh.newsreader.articles.data.model.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArticleRepository {

    fun getNews(pageNumber: Int = DEFAULT_PAGE_NUM){

    }

    fun getNewsByCountry(
        countryCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ) {
    }

    fun getNewsByLanguage(
        languageCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ){

    }

    fun getNewsBySource(
        sourceCode: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ){

    }

    fun searchNews(
        searchQuery: String,
        pageNumber: Int = DEFAULT_PAGE_NUM
    ){

    }

    // Get BookMarked News Articles
    fun getSavedNews(){

    }

    suspend fun getCountries(): Flow<List<Country>> = flow {
        emit(AppConstants.countryList)
    }

    suspend fun getLanguages(): Flow<List<Language>> = flow {
        emit(AppConstants.languageList)
    }
}