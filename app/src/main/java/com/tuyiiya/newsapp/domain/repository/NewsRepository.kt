package com.tuyiiya.newsapp.domain.repository

import com.tuyiiya.newsapp.data.response.NewsResponse
import retrofit2.Response

interface NewsRepository {
    suspend fun getNews(
        language: String,
        text: String?,
        country: String?
    ): Response<NewsResponse>
}