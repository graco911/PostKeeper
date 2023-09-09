package com.gracodev.postkeeper.data.interfaces

import com.gracodev.postkeeper.data.models.NewsResponseData
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("top-headlines")
    fun getNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): Deferred<Response<NewsResponseData>>
}