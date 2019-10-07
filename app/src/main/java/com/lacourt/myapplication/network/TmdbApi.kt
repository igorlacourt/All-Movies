package com.lacourt.myapplication.network

import com.lacourt.myapplication.model.GenreResponse
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.MovieResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi{
//    @GET("movie/upcoming")
//    fun getMovies() : Call<MovieResponse>
//
//    @GET("movie/upcoming")
//    fun getMovies(
//
//        @Query("language")
//        language: String,
//
//        @Query("page")
//        page: Int): Call<MovieResponse>
//
//    @GET("genre/movie/list")
//    fun getGenres() : Call<GenreResponse>

    @GET("movie/upcoming")
    fun getMoviesObservable(

        @Query("language")
        language: String,

        @Query("page")
        page: Int): Observable<MovieResponse>

    @GET("genre/movie/list")
    fun getGenresObservable() : Observable<GenreResponse>

    @GET("search/movie")
    fun searchMovie(
        @Query("language")
        language: String,

        @Query("query")
        query: String,

        @Query("include_adult")
        adult: Boolean

    ) : Call<MovieResponse>

}