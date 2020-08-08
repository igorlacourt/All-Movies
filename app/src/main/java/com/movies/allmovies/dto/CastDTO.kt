package com.movies.allmovies.dto

import com.google.gson.annotations.SerializedName

data class CastDTO(
    val castId: Int?,
    val character: String?,
    val creditId: String?,
    val gender: Int?,
    val id: Int?,
    val name: String?,
    val order: Int?,
    @SerializedName("profile_path")
    val profilePath: String?
)