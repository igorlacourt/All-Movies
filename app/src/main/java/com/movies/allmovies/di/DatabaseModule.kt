package com.movies.allmovies.di

import android.content.Context
import androidx.room.Room
import com.movies.allmovies.AppConstants.DATABASE_NAME
import com.movies.allmovies.database.MyListDao
import com.movies.allmovies.database.RoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabase(context: Context): RoomDatabase =
        Room.databaseBuilder(context.applicationContext, RoomDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun providesMyListDao(db: RoomDatabase): MyListDao {
        return db.MyListDao()
    }
}