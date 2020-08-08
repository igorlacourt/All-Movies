package com.movies.allmovies.dto

import java.io.Serializable

data class MovieDTO(
    val id: Int?,
    val poster_path: String?,
    val backdrop_path: String?,
    val title: String?
) : Serializable




