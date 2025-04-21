package com.tuyiiya.newsapp.data.repository

import com.tuyiiya.newsapp.data.response.NewsResponse
import com.tuyiiya.newsapp.data.web.NewsApi
import com.tuyiiya.newsapp.domain.repository.NewsRepository
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(val api: NewsApi) : NewsRepository {
    override suspend fun getNews(language: String, text: String?, country: String?): Response<NewsResponse> {
        return api.getNews(country, language, text)
    }
}