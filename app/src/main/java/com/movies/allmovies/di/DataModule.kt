package com.movies.allmovies.di

import com.movies.allmovies.ui.search.SearchRepository
import com.movies.allmovies.repository.SearchRepositoryImpl
import com.movies.allmovies.viewmodel.HomeDataSource
import com.movies.allmovies.viewmodel.HomeDataSourceImpl
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