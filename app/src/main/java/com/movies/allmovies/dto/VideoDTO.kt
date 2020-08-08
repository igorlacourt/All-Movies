package com.movies.allmovies.dto

import com.google.gson.annotations.Expose
import java.io.Serializable

data class VideoDTO(
    val id: String?,
    val iso_639_1: String?,
    val iso_3166_1: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
) : Serializable