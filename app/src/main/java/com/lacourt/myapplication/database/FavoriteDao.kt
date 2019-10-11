package com.lacourt.myapplication.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.lacourt.myapplication.model.Favorite
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.dbMovie.DbMovie

@Dao
interface FavoriteDao {
    @Insert
    fun insert(dbMovie: DbMovie)

    @Query("DELETE FROM favorites WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM favorites ORDER BY release_date DESC")
    fun dateDesc(): DataSource.Factory<Int, DbMovie>

    @Query("SELECT * FROM favorites ORDER BY release_date ASC")
    fun dateAsc(): DataSource.Factory<Int, DbMovie>
}