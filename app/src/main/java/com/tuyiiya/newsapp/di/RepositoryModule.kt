package com.tuyiiya.newsapp.di

import com.tuyiiya.newsapp.data.repository.NewsRepositoryImpl
import com.tuyiiya.newsapp.data.web.NewsApi
import com.tuyiiya.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsApi): NewsRepository {
        return NewsRepositoryImpl(api)
    }
}