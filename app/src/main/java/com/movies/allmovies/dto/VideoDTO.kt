package com.movies.allmovies.dto

import com.google.gson.annotations.Expose
import java.io.Serializable

data class VideoDTO(
    @Expose
    val id: String?,

    @Expose
    val iso_639_1: String?,

    @Expose
    val iso_3166_1: String?,

    @Expose
    val key: String?,

    @Expose
    val name: String?,

    @Expose
    val site: String?,

    @Expose
    val size: Int?,

    @Expose
    val type: String?


) : Serializable