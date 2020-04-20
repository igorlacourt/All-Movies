package com.movies.allmovies.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
    @SerializedName("profile_path")
    val profilePath: String?
)