package com.lacourt.myapplication.network

import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.dto.GenreResponseDTO
import com.lacourt.myapplication.dto.MovieResponseDTO
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi{
//    @GET("movie/upcoming")
//    fun getMovies() : Call<MovieResponseDTO>
//

    @GET("movie/{id}")
    fun getDetails(
        @Path("id")
        id: Int
    ): Call<DetailsDTO>

    @GET("movie/upcoming")
    fun getMovies(

        @Query("language")
        language: String,

        @Query("page")
        page: Int): Call<MovieResponseDTO>
//
//    @GET("genre/movie/list")
//    fun getGenreDTOS() : Call<GenreResponseDTO>

    @GET("movie/upcoming")
    fun getMoviesObservable(

        @Query("language")
        language: String,

        @Query("page")
        page: Int): Observable<MovieResponseDTO>

    @GET("genre/movie/list")
    fun getGenresObservable() : Observable<GenreResponseDTO>

    @GET("search/movie")
    fun searchMovie(
        @Query("language")
        language: String,

        @Query("query")
        query: String,

        @Query("include_adult")
        adult: Boolean

    ) : Call<MovieResponseDTO>

    @GET("movie/popular")
    fun getPopularMovies(

        @Query("language")
        language: String,

        @Query("page")
        page: Int): Call<MovieResponseDTO>
}