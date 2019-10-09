package com.lacourt.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lacourt.myapplication.database.AppDatabase.Companion.DATABASE_VERSION
import com.lacourt.myapplication.model.Favorite
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.dbMovie.DbMovie

private var INSTANCE: AppDatabase? = null

@Database(entities = [DbMovie::class, Favorite::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun MovieDao() : MovieDao
//TODO(create the favorites table)
    companion object {
        const val DATABASE_VERSION = 6
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