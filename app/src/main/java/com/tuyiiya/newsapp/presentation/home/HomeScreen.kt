package com.tuyiiya.newsapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.tuyiiya.newsapp.R
import com.tuyiiya.newsapp.data.model.News
import com.tuyiiya.newsapp.presentation.State
import com.tuyiiya.newsapp.utils.NavRoute


@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    val searchText = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // TODO() - SEARCH BAR
        SearchBar(text = searchText.value, onSearch = {
            searchText.value = it
            viewModel.getNews(text = it)
        })
        Spacer(modifier = Modifier.height(16.dp))

        // TODO() - CATEGORIES

        when (uiState.value) {
            is State.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(text = "Loading...")
                }
            }

            is State.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Something went wrong!")
                    Text(text = (uiState.value as State.Error).error)
                    Button(
                        onClick = { viewModel.getNews(searchText.value) }
                    ) {
                        Text(text = "Reload")
                    }
                }
            }

            else -> {
                val data = (uiState.value as State.Success).data

                NewsListView(news = data.news) {
                    val route = NavRoute.createNewsDetailsRoute(it)
                    navController.navigate(route)
                }
            }
        }
    }
}


@Composable
fun NewsListView(news: List<News>, onClick: (News) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(text = "News")
        }
        items(news) { article ->
            NewsItem(article, onClick = { onClick(article) })
        }
    }
}


@Composable
fun NewsItem(news: News, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Red.copy(alpha = 0.2f))
            .clickable { onClick() }
    ) {
        //val imageUrl = news.image ?: ""
        //Log.d("NewsItem", "Loading image: $imageUrl")
        AsyncImage(
            model = news.image,
            contentDescription = news.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.news_default),
            error = painterResource(id = R.drawable.news_default)
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)) {
            Text(
                text = news.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
            Text(
                text = news.publish_date,
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
            Text(
                text = news.authors?.joinToString(", ") ?: "Unknown",
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@Composable
fun SearchBar(text: String, onSearch: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = onSearch,
            label = { Text(text = "Search") },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(id = R.drawable.ic_menu_search),
            contentDescription = "search bar",
            modifier = Modifier
                .padding(end = 16.dp)
                .align(Alignment.CenterEnd)
        )
    }
}