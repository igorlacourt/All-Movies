package com.movies.allmovies.dto

data class DetailsDTO(
    val adult: Boolean,
    val backdrop_path: String,
    val genres: List<GenreXDTO>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val runtime: Int,
    val status: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val videos: VideosDTO?,
    val casts: CastsDTO?
)