package com.movies.allmovies.di

import com.movies.allmovies.ui.search.SearchRepository
import com.movies.allmovies.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {
    @Singleton
    @Binds
    abstract fun provideLocalDataSource(repository: SearchRepositoryImpl): SearchRepository
}