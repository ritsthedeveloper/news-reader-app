package com.ritesh.newsreader.articles.application

import com.ritesh.newsreader.AppConstants
import com.ritesh.newsreader.articles.data.repository.ArticleRepositoryV2
import com.ritesh.newsreader.articles.data.repository.database.entity.Article
import com.ritesh.newsreader.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticlesUseCase @Inject constructor(
    private val newsRepository: ArticleRepositoryV2,
    private val logger: Logger
) {

    private var pageNum = AppConstants.DEFAULT_PAGE_NUM

    suspend fun getNews(page: Int = AppConstants.DEFAULT_PAGE_NUM): List<Article> {
        logger.logDebug("NewsViewModel", "Inside ArticleUseCase - getNews")
        return newsRepository.getNews(page)
    }

    fun getNewsByCountry(countryId: String?): Flow<List<Article>> = flow {
        logger.logDebug("NewsViewModel", "Inside ArticleUseCase - fetchNewsByCountry")
        emit(
            newsRepository.getNewsByCountryV2(
                countryId ?: AppConstants.DEFAULT_COUNTRY,
                pageNumber = pageNum
            )
        )
    }

    fun getNewsByLanguage(languageId: String?): Flow<List<Article>> = flow {
        logger.logDebug("NewsViewModel", "Inside ArticleUseCase - getNewsByLanguage")
        emit(
            newsRepository.getNewsByLanguageV2(
                languageId ?: AppConstants.DEFAULT_LANGUAGE,
                pageNumber = pageNum
            )
        )
    }

    fun getNewsBySource(sourceId: String?): Flow<List<Article>> = flow {
        logger.logDebug("NewsViewModel", "Inside ArticleUseCase - getNewsBySource")
        emit(
            newsRepository.getNewsBySourceV2(
                sourceId ?: AppConstants.DEFAULT_SOURCE,
                pageNumber = pageNum
            )
        )
    }

    fun getNewsByCategory(categoryId: String?): Flow<List<Article>> = flow {
        logger.logDebug("NewsViewModel", "Inside ArticleUseCase - fetchNewsByCategory")
        emit(
            newsRepository.getNewsByCategoryV2(
                categoryId ?: AppConstants.DEFAULT_CATEGORY,
                pageNumber = pageNum
            )
        )
    }
}