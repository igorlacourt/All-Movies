package com.movies.allmovies.dto

import com.google.gson.annotations.SerializedName

data class CrewDTO(
    val creditId: String?,
    val department: String?,
    val gender: Int?,
    val id: Int?,
    val job: String?,
    val name: String?,
    @SerializedName("profile_path")
    val profilePath: String?
)