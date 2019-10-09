package com.lacourt.myapplication.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.dbMovie.DbMovie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: DbMovie)

    @Update
    fun update(movie: DbMovie)

    @Query("SELECT * FROM movies ORDER BY release_date DESC")
    fun dateDesc(): DataSource.Factory<Int, DbMovie>

    @Query("SELECT * FROM movies ORDER BY release_date")
    fun dateAsc(): DataSource.Factory<Int, DbMovie>

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<DbMovie>>

    @Query("SELECT * FROM movies WHERE id=:id")
    fun getMovieById(id: Int): DbMovie?

    @Query("SELECT COUNT(id) FROM movies")
    fun getCount(): Int

    @Query ("DELETE FROM movies")
    fun deleteAll()

//    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'")
//    fun searchDbMovie(search: String?): LiveData<List<DbMovie>>
//
//    @Query("SELECT * FROM movies WHERE id=:id" )
//    fun getMovieById(id: Int?): Movie
//
//    @Query("DELETE FROM movies WHERE id=:id")
//    fun deleteMovieById(id:Int)
//
//    @Delete
//    fun deleteMovie(card: Movie)
//

}