package com.movies.allmovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.movies.allmovies.database.AppDatabase.Companion.DATABASE_VERSION
import com.movies.allmovies.domainmodel.MyListItem

private var INSTANCE: AppDatabase? = null

@Database(entities = [MyListItem::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun MyListDao() : MyListDao

    companion object {
        const val DATABASE_VERSION = 1
        val DATABASE_NAME = "app_database"

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Create database here
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                AppDatabase::class.java, DATABASE_NAME)
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}