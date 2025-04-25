package com.tuyiiya.newsapp.presentation.news_details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.tuyiiya.newsapp.R
import com.tuyiiya.newsapp.data.model.News
import com.tuyiiya.newsapp.presentation.State

@Composable
fun NewsDetailsScreen(navController: NavController, news: News, isLocal: Boolean = false) {
    NewsDetails(navController = navController, news = news, isLocal = isLocal)
}


@Composable
fun NewsDetails(navController: NavController, news: News, isLocal: Boolean = false) {
    val viewModel: NewsDetailsViewModel = hiltViewModel()

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = news.image,
            contentDescription = news.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.news_default),
            error = painterResource(id = R.drawable.news_default)
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val (backBtn, topSpace, summary, newContent) = createRefs()

            Spacer(
                modifier = Modifier
                    .height(350.dp)
                    .constrainAs(topSpace) {
                        top.linkTo(parent.top)
                    }
            )

            Image(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back arrow",
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .padding(vertical = 50.dp, horizontal = 16.dp)
                    .constrainAs(newContent) {
                        top.linkTo(topSpace.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.wrapContent
                    }
            ) {
                Text(
                    text = news.text,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .width(300.dp)
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .padding(16.dp)
                    .constrainAs(summary) {
                        top.linkTo(topSpace.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(topSpace.bottom)
                    }
            ) {
                Text(text = news.publish_date, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = news.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = news.authors?.joinToString(", ") ?: "", fontSize = 10.sp)
            }
        }

        Image(
            imageVector = if (isLocal) Icons.Filled.Delete else Icons.Filled.Favorite,
            contentDescription = if (isLocal) "Remove from bookmarks" else "Add to bookmarks",
            modifier = Modifier
                .padding(16.dp)
                .clip(CircleShape)
                .background(Color.Red)
                .height(48.dp)
                .width(48.dp)
                .padding(8.dp)
                .align(Alignment.BottomEnd)
                .clickable {
                    if (isLocal)
                        viewModel.removeNews(news)
                    else
                        viewModel.addNews(news)
                }
        )
    }

    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(state.value) {
        viewModel.checkIfNewsIsBookmarked(news)
        if (state.value is State.Loading) {
            return@LaunchedEffect
        }
        if (state.value is State.Success) {
            Toast.makeText(context, "News added to database successfully!", Toast.LENGTH_SHORT).show()
            viewModel.checkIfNewsIsBookmarked(news)

            if((state.value as State.Success<BookmarkAction>).data == BookmarkAction.REMOVE) {
                navController.popBackStack()
            }
        } else {
            Toast.makeText(context, "News failed to add to database", Toast.LENGTH_SHORT).show()
        }
    }
}
