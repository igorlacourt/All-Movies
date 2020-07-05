package com.movies.allmovies.dto

import com.google.gson.annotations.Expose

data class MovieResponseDTO(
    @Expose
    val results: List<MovieDTO>
)