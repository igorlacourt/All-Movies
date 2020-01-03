package com.movies.allmovies.dto

import com.google.gson.annotations.Expose

data class CastDTO(
    @Expose
    val castId: Int?,
    @Expose
    val character: String?,
    @Expose
    val creditId: String?,
    @Expose
    val gender: Int?,
    @Expose
    val id: Int?,
    @Expose
    val name: String?,
    @Expose
    val order: Int?,
    @Expose
    val profilePath: String?
)