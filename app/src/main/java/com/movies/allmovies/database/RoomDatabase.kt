package com.movies.allmovies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.movies.allmovies.AppConstants.DATABASE_VERSION
import com.movies.allmovies.domainmodel.MyListItem

@Database(entities = [MyListItem::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun MyListDao() : MyListDao

}