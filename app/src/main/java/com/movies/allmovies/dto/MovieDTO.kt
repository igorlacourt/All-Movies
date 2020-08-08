package com.movies.allmovies.dto

import androidx.annotation.NonNull
import com.google.gson.annotations.Expose
import java.io.Serializable

data class MovieDTO(
    @Expose
    @NonNull
    val id: Int?,

    @Expose
    val poster_path: String?,

    @Expose
    val backdrop_path: String?,

    @Expose
    val title: String?

) : Serializable




