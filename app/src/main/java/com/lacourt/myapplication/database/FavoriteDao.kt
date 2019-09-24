package com.lacourt.myapplication.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lacourt.myapplication.model.Movie

@Dao
interface FavoriteDao {
    @Insert
    fun insertCard(card: Movie)

    @Query("SELECT * FROM favorites ORDER BY release_date DESC")
    fun moviesByDateDesc(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM favorites ORDER BY release_date ASC")
    fun moviesByDateAsc(): DataSource.Factory<Int, Movie>
}