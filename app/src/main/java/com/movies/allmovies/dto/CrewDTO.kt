package com.movies.allmovies.dto

import com.google.gson.annotations.Expose

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
    val profilePath: Any?
)