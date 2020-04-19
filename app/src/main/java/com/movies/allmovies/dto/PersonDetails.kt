package com.movies.allmovies.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PersonDetails(
    @Expose
    val id: Int?,

    @Expose
    val name: String?,

    @Expose
    val biography: String?,

    @Expose
    @SerializedName("profile_path")
    val profilePath: String?,

    @Expose
    val popularity: String?,

    @Expose
    @SerializedName("place_of_birth")
    val placeOfBirth: String?,

    @Expose
    val gender: Int?,

    @Expose
    val birthday: String?,

    @Expose
    val deathday: String?,

    @Expose
    @SerializedName("known_for_department")
    val department: String?
)