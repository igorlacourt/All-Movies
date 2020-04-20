package com.movies.allmovies.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CrewDTO(
    @Expose
    val creditId: String?,
    @Expose
    val department: String?,
    @Expose
    val gender: Int?,
    @Expose
    val id: Int?,
    @Expose
    val job: String?,
    @Expose
    val name: String?,
    @Expose
    @SerializedName("profile_path")
    val profilePath: String?
)