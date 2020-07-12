package com.movies.allmovies.di

import com.movies.allmovies.repository.HomeDataSource
import com.movies.allmovies.repository.HomeDataSourceImpl
import com.movies.allmovies.ui.search.SearchRepository
import com.movies.allmovies.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {
    @Singleton
    @Binds
    abstract fun provideSearchDataSource(repository: SearchRepositoryImpl): SearchRepository

    @Singleton
    @Binds
    abstract fun provideHomeDataSource(datasource: HomeDataSourceImpl): HomeDataSource
}