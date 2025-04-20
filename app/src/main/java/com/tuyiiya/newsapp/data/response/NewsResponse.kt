package com.tuyiiya.newsapp.data.response

import com.tuyiiya.newsapp.data.model.News

data class NewsResponse(
    val available: Int,
    val news: List<News>,
    val number: Int,
    val offset: Int
)