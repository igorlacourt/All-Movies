package com.movies.allmovies.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.allmovies.domainmodel.MyListItem

@Dao
interface MyListDao {
    @Query("SELECT EXISTS (SELECT id FROM mylist WHERE id = :id)")
    fun exists(id: Int?): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MyListItem)

    @Query("SELECT * FROM mylist")
    fun all(): List<MyListItem>

    @Query("DELETE FROM mylist WHERE id = :id")
    fun deleteById(id: Int)
}