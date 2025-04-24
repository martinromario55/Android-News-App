package com.tuyiiya.newsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.tuyiiya.newsapp.data.model.News
import com.tuyiiya.newsapp.presentation.home.HomeScreen
import com.tuyiiya.newsapp.presentation.news_details.NewsDetailsScreen
import com.tuyiiya.newsapp.ui.theme.NewsAppTheme
import com.tuyiiya.newsapp.utils.NavRoute
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { HomeScreen(navController = navController) }

                        composable("details/news={news}") { backStackEntry ->
                            val newsJson = backStackEntry.arguments?.getString("news")
                            if (newsJson != null) {
                                val decodedJson = URLDecoder.decode(newsJson, "utf-8")
                                val news = Gson().fromJson(decodedJson, News::class.java)
                                NewsDetailsScreen(navController = navController, news = news)
                            }
                        }
                    }
                }
            }
        }
    }
}
