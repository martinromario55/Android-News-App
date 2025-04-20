package com.tuyiiya.newsapp.domain.repository

import com.tuyiiya.newsapp.data.response.NewsResponse

interface NewsRepository {
    suspend fun getNews(
        language: String,
        text: String?,
        country: String?
    ): NewsResponse
}