package com.movies.allmovies.dto

import java.io.Serializable

data class VideosDTO(
    val results: ArrayList<VideoDTO>?
) : Serializable