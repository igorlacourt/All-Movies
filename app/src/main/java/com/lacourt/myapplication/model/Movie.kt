package com.lacourt.myapplication.model

import androidx.annotation.NonNull
import com.google.gson.annotations.Expose
import java.io.Serializable

data class Movie(
    @Expose
    val adult: Boolean?,

    @Expose
    val backdrop_path: String?,

    @Expose
    val genre_ids: ArrayList<String>?,

    @Expose
    var genres: ArrayList<String>?,

    @Expose
    @NonNull
    val id: Int,

    @Expose
    val original_language: String?,

    @Expose
    val original_title: String?,

    @Expose
    val overview: String?,

    @Expose
    val popularity: Double?,

    @Expose
    val poster_path: String?,

    @Expose
    val release_date: String?,

    @Expose
    val title: String?,

    @Expose
    val video: Boolean?,

    @Expose
    val vote_average: Double?,

    @Expose
    val vote_count: Int?
) : Serializable