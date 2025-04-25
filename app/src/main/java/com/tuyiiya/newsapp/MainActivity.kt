package com.tuyiiya.newsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.tuyiiya.newsapp.data.model.News
import com.tuyiiya.newsapp.presentation.bookmarks.BookmarkScreen
import com.tuyiiya.newsapp.presentation.home.HomeScreen
import com.tuyiiya.newsapp.presentation.news_details.NewsDetailsScreen
import com.tuyiiya.newsapp.ui.theme.NewsAppTheme
import com.tuyiiya.newsapp.utils.NavRoute
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                val isBottomBarVisible = remember {
                    mutableStateOf(true)
                }

                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(visible = isBottomBarVisible.value) {
                            BottomAppBar {
                                val currentRoute =
                                    navController.currentBackStackEntryAsState().value?.destination?.route

                                bottomNavItems.forEach {
                                    NavigationBarItem(
                                        icon = {
                                            Image(
                                                imageVector = it.icon,
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text(text = it.title) },
                                        selected = currentRoute == it.route,
                                        onClick = {
                                            navController.navigate(it.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = NavScreen.Home.route
                        ) {
                            composable(NavScreen.Home.route) {
                                HomeScreen(navController = navController)
                                isBottomBarVisible.value = true
                            }

                            composable("details/news={news}&isLocal={isLocal}") { backStackEntry ->
                                val newsJson = backStackEntry.arguments?.getString("news")
                                val isLocal =
                                    backStackEntry.arguments?.getString("isLocal").toBoolean()

                                if (newsJson != null) {
                                    val decodedJson = URLDecoder.decode(newsJson, "utf-8")
                                    val news = Gson().fromJson(decodedJson, News::class.java)
                                    NewsDetailsScreen(
                                        navController = navController,
                                        news = news,
                                        isLocal = isLocal
                                    )
                                    isBottomBarVisible.value = false
                                }
                            }

                            composable(NavScreen.Bookmarks.route) {
                                BookmarkScreen(navController)
                                isBottomBarVisible.value = true
                            }
                        }
                    }
                }
            }
        }
    }
}


sealed class NavScreen(val route: String, val icon: ImageVector, val title: String) {
    object Home : NavScreen("home", Icons.Default.Home, "Home")

    object Bookmarks : NavScreen("bookmarks", Icons.Default.Favorite, "Bookmarks")
}

val bottomNavItems = listOf(
    NavScreen.Home,
    NavScreen.Bookmarks
)