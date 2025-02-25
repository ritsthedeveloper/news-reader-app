package com.ritesh.newsreader.articles.data.repository.network

import com.ritesh.newsreader.di.ApiKey
import com.ritesh.newsreader.logger.Logger
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiKeyInterceptor @Inject constructor(
    @ApiKey private val apiKey: String,
    private val logger: Logger,
) : Interceptor {

    @Throws(IOException::class)
    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("X-Api-Key", apiKey)
        val request = requestBuilder.build()
        logger.logDebug("NewsReader", "Request Header :" + request.headers)
        logger.logDebug("NewsReader", "Request URL :" + request.url)
        val response : Response = chain.proceed(request)

        if(response.isSuccessful){
            logger.logDebug("NewsReader", "Response Body:$response")
        }
        return response
    }
}