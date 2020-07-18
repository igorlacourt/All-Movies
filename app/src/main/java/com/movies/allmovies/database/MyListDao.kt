package com.movies.allmovies.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.DbMovieDTO
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MyListDao {
    @Query("SELECT EXISTS (SELECT id FROM mylist WHERE id = :id)")
    fun exists(id: Int): Boolean
//
//    @Query("SELECT id FROM mylist WHERE id = :id")
//    fun getById(id: Int): Single<Int>

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