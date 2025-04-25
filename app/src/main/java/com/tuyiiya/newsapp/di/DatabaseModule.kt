package com.tuyiiya.newsapp.di

import android.content.Context
import com.tuyiiya.newsapp.data.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): NewsDatabase {
        return NewsDatabase.getDatabase(context)
    }
}