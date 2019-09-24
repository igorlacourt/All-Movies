package com.lacourt.myapplication.network

import com.lacourt.myapplication.model.GenreResponse
import com.lacourt.myapplication.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi{
//    @GET("movie/upcoming")
//    fun getMovies() : Call<MovieResponse>

    @GET("movie/upcoming")
    fun getMovies(

        @Query("language")
        language: String,

        @Query("page")
        page: Int): Call<MovieResponse>

    @GET("genre/movie/list")
    fun getGenres() : Call<GenreResponse>

}