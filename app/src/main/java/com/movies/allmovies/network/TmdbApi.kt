package com.movies.allmovies.network

import com.movies.allmovies.dto.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("language")
        language: String,
        @Query("query")
        query: String,
        @Query("include_adult")
        adult: Boolean
    ): NetworkResponse<MovieResponseDTO, Error>

    //    https://api.themoviedb.org/3/movie/287947/recommendations?api_key=fef98cf6bd829f53836bb7d92b02d6ef&language=en-US&page=1
    @GET("movie/{id}/recommendations")
    suspend fun getRecommendations(
        @Path("id")
        id: Int?,
        @Query("language")
        language: String,
        @Query("page")
        page: Int
    ): NetworkResponse<MovieResponseDTO, Error>

    @GET("movie/{id}")
    suspend fun getDetails(
        @Path("id")
        id: Int?,
        @Query("append_to_response")
        append: String? = null
    ): NetworkResponse<DetailsDTO, Error>

    @GET("person/{person_id}")
    fun getPerson(@Path("person_id") person_id: Int): Call<PersonDetails>

    @GET("discover/movie")
    fun getActorsMovies(
        @Query("with_cast")
        id: Int,
        @Query("language")
        language: String,
        @Query("include_adult")
        adult: Boolean
    ): Call<MovieResponseDTO>

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
    ): NetworkResponse<MovieResponseDTO, Error>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
    ): NetworkResponse<MovieResponseDTO, Error>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
    ): NetworkResponse<MovieResponseDTO, Error>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
    ): NetworkResponse<MovieResponseDTO, Error>
}