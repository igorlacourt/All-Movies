package com.movies.allmovies.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.movies.allmovies.dto.DbMovieDTO

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieDTO: DbMovieDTO)

    @Update
    fun update(movieDTO: DbMovieDTO)

    @Query("SELECT * FROM movies ORDER BY release_date DESC")
    fun dateDesc(): DataSource.Factory<Int, DbMovieDTO>

    @Query("SELECT * FROM movies ORDER BY release_date")
    fun dateAsc(): DataSource.Factory<Int, DbMovieDTO>

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<DbMovieDTO>>

    @Query("SELECT * FROM movies WHERE id=:id")
    fun getMovieById(id: Int): DbMovieDTO?

    @Query("SELECT COUNT(id) FROM movies")
    fun getCount(): Int

    @Query ("DELETE FROM movies")
    fun deleteAll()

//    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'")
//    fun searchDbMovie(search: String?): LiveData<List<DbMovieDTO>>
//
//    @Query("SELECT * FROM movies WHERE id=:id" )
//    fun getMovieById(id: Int?): MovieDTO
//
//    @Query("DELETE FROM movies WHERE id=:id")
//    fun deleteMovieById(id:Int)
//
//    @Delete
//    fun deleteMovie(card: MovieDTO)
//

}