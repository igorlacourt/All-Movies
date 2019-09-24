package com.lacourt.myapplication.model

import com.google.gson.annotations.Expose

data class MovieResponse(
    @Expose
    val results: List<Movie>
)