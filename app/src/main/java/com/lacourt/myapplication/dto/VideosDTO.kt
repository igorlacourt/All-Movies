package com.lacourt.myapplication.dto

import com.google.gson.annotations.Expose
import java.io.Serializable

data class VideosDTO(
    @Expose
    val results: ArrayList<VideoDTO>?
) : Serializable