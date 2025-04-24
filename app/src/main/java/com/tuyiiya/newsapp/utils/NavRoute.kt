package com.tuyiiya.newsapp.utils

import com.google.gson.Gson
import com.tuyiiya.newsapp.data.model.News
import java.net.URLDecoder
import java.net.URLEncoder

object NavRoute {
    fun createNewsDetailsRoute(news: News): String {
        val gson = Gson().toJson(news)
        val encodedJson = URLEncoder.encode(gson, "utf-8")
        return "details/news=$encodedJson"
    }

    fun getNewsFromRoute(json: String): News {
        val news = Gson().fromJson(json, News::class.java)
        val decodeImage = URLDecoder.decode(news.image, "utf-8")
        val decodeUrl = URLDecoder.decode(news.url, "utf-8")

        return news.copy(image = decodeImage, url = decodeUrl)
    }
}