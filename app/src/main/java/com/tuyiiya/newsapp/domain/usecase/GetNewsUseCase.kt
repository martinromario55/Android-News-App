package com.tuyiiya.newsapp.domain.usecase

import com.tuyiiya.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(
        language: String,
        text: String?,
        country: String?
    ) = newsRepository.getNews(language, text, country)
}