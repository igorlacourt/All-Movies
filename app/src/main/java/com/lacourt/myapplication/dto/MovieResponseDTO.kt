package com.lacourt.myapplication.dto

import com.google.gson.annotations.Expose

data class MovieResponseDTO(
    @Expose
    val results: ArrayList<MovieDTO>
)