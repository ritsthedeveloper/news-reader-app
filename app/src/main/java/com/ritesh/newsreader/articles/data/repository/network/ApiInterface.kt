package com.ritesh.newsreader.articles.data.repository.network

import com.ritesh.newsreader.AppConstants.DEFAULT_CATEGORY
import com.ritesh.newsreader.AppConstants.DEFAULT_COUNTRY
import com.ritesh.newsreader.AppConstants.DEFAULT_LANGUAGE
import com.ritesh.newsreader.AppConstants.DEFAULT_PAGE_NUM
import com.ritesh.newsreader.AppConstants.DEFAULT_QUERY_PAGE_SIZE
import com.ritesh.newsreader.AppConstants.DEFAULT_SOURCE
import com.ritesh.newsreader.articles.data.model.ArticlesResponse
import com.ritesh.newsreader.sources.data.model.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String = DEFAULT_COUNTRY,
        @Query("page") pageNum: Int = DEFAULT_PAGE_NUM,
        @Query("pageSize") pageSize: Int = DEFAULT_QUERY_PAGE_SIZE,
    ): ArticlesResponse

    @GET("top-headlines")
    suspend fun getNewsByLang(
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") pageNum: Int = DEFAULT_PAGE_NUM,
        @Query("pageSize") pageSize: Int = DEFAULT_QUERY_PAGE_SIZE,
    ): ArticlesResponse

    @GET("top-headlines")
    suspend fun getNewsBySource(
        @Query("sources") sources: String = DEFAULT_SOURCE,
        @Query("page") pageNum: Int = DEFAULT_PAGE_NUM,
        @Query("pageSize") pageSize: Int = DEFAULT_QUERY_PAGE_SIZE,
    ): ArticlesResponse

    @GET("top-headlines")
    suspend fun getNewsByCategory(
        @Query("category") category: String = DEFAULT_CATEGORY,
        @Query("page") pageNum: Int = DEFAULT_PAGE_NUM,
        @Query("pageSize") pageSize: Int = DEFAULT_QUERY_PAGE_SIZE,
    ): ArticlesResponse

    @GET("everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNum: Int = DEFAULT_PAGE_NUM,
        @Query("pageSize") pageSize: Int = DEFAULT_QUERY_PAGE_SIZE,
    ): ArticlesResponse

    @GET("sources")
    suspend fun getSources(): SourcesResponse
}