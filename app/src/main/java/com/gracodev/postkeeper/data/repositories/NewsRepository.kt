package com.gracodev.postkeeper.data.repositories

import com.gracodev.postkeeper.data.interfaces.NewsAPI
import com.gracodev.postkeeper.data.models.NewsResponseData
import com.gracodev.postkeeper.data.usecases.UseCaseResult
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface NewsRepository {
    suspend fun getNews(country: String, category: String, apiKey: String): UseCaseResult<NewsResponseData>
}

class NewsRepositoryImpl(private val newsAPI: NewsAPI) : NewsRepository {
    private suspend fun <T : Any> executeRequest(apiCall: suspend () -> Deferred<Response<T>>): UseCaseResult<T> {
        return try {
            val response = apiCall.invoke().await()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    UseCaseResult.Success(body)
                } else {
                    UseCaseResult.Error(Exception("Response body is null"))
                }
            } else {
                UseCaseResult.Error(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    override suspend fun getNews(
        country: String,
        category: String,
        apiKey: String
    ): UseCaseResult<NewsResponseData> {
        return executeRequest { newsAPI.getNews(country, category, apiKey) }
    }
}