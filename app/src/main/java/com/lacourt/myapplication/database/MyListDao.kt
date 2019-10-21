package com.lacourt.myapplication.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DbMovieDTO

@Dao
interface MyListDao {
    @Query("SELECT id FROM mylist WHERE id = :id")
    fun getById(id: Int): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MyListItem)

    @Query("SELECT * FROM mylist")
    fun all(): List<MyListItem>

    @Query("DELETE FROM mylist WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM mylist ORDER BY release_date DESC")
    fun dateDesc(): DataSource.Factory<Int, DbMovieDTO>

    @Query("SELECT * FROM mylist ORDER BY release_date ASC")
    fun dateAsc(): DataSource.Factory<Int, DbMovieDTO>
}