package com.lacourt.myapplication.model.dto

import com.google.gson.annotations.Expose

data class MovieResponse(
    @Expose
    val results: ArrayList<Movie>
)