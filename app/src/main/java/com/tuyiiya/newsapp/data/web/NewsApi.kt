package com.tuyiiya.newsapp.data.web

import com.tuyiiya.newsapp.data.response.NewsResponse
import com.tuyiiya.newsapp.utils.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {

    @GET("search-news")
    suspend fun getNews(
        @Query("country") country: String?,
        @Query("language") language: String,
        @Query("text") text: String?,
        @Query("api-key") apiKey: String = API_KEY
    ): Response<NewsResponse>
}