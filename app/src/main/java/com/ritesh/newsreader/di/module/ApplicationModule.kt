package com.ritesh.newsreader.di.module

import android.app.Application
import androidx.room.Room
import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.repository.database.AppDatabaseService
import com.ritesh.newsreader.articles.data.repository.database.ArticleDatabase
import com.ritesh.newsreader.articles.data.repository.database.DatabaseService
import com.ritesh.newsreader.articles.data.repository.network.ApiInterface
import com.ritesh.newsreader.articles.data.repository.network.ApiKeyInterceptor
import com.ritesh.newsreader.di.ApiKey
import com.ritesh.newsreader.di.BaseUrl
import com.ritesh.newsreader.di.DbName
import com.ritesh.newsreader.logger.AppLogger
import com.ritesh.newsreader.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @ApiKey
    @Provides
    fun provideApiKey(): String = AppConstants.API_KEY

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = AppConstants.BASE_URL

    @DbName
    @Provides
    fun provideDbName(): String = AppConstants.DB_NAME

    @Provides
    @Singleton
    fun provideLogger(): Logger = AppLogger

    @Singleton
    @Provides
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonFactory: GsonConverterFactory,
        apiKeyInterceptor: ApiKeyInterceptor
    ): ApiInterface {
        val client = OkHttpClient
            .Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()

        return Retrofit
            .Builder()
            .client(client) //adding client to intercept all responses
            .baseUrl(baseUrl)
            .addConverterFactory(gsonFactory)
            .build()
            .create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideArticleDatabase(
        application: Application,
        @DbName dbName: String
    ): ArticleDatabase {
        return Room.databaseBuilder(
            application,
            ArticleDatabase::class.java,
            dbName
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabaseService(articleDatabase: ArticleDatabase): DatabaseService {
        return AppDatabaseService(articleDatabase)
    }
}