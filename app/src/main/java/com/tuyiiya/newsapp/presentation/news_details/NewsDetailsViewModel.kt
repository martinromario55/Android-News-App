package com.tuyiiya.newsapp.presentation.news_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuyiiya.newsapp.data.database.NewsDatabase
import com.tuyiiya.newsapp.data.model.News
import com.tuyiiya.newsapp.presentation.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(database: NewsDatabase) : ViewModel() {
    private val _state = MutableStateFlow<State<BookmarkAction>>(State.Loading)
    val state = _state as StateFlow<State<BookmarkAction>>

    private val newsDao = database.newsDao()

    private val _isLocal = MutableStateFlow(false)
    val isLocal: StateFlow<Boolean> = _isLocal

    init {
        // Initially set to false
        _isLocal.tryEmit(false)
    }

    fun checkIfNewsIsBookmarked(news: News) {
        viewModelScope.launch {
            val isSaved = newsDao.getNewsById(news.id!!) != null
            _isLocal.emit(isSaved)
        }
    }

    fun addNews(news: News) {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                newsDao.addNews(news)
                _state.tryEmit(State.Success(BookmarkAction.ADD))
            } catch (e: Exception) {
                _state.tryEmit(State.Error(e.message.toString()))
            }
        }
    }

    fun removeNews(news: News) {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                newsDao.deleteNews(news)
                _state.tryEmit(State.Success(BookmarkAction.REMOVE))
            } catch (e: Exception) {
                _state.tryEmit(State.Error(e.message.toString()))
            }
        }
    }
}

enum class BookmarkAction {
    ADD, REMOVE
}