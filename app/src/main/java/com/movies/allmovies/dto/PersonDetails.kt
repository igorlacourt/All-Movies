package com.movies.allmovies.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*


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
) {
    private fun birthYear(): Int { return toYears(SimpleDateFormat("yyyy-mm-dd", Locale.US).parse(birthday).time).toInt()}
    private fun deathYear(): Int { return toYears(SimpleDateFormat("yyyy-mm-dd", Locale.US).parse(deathday).time).toInt()}
    private fun currentYear(): Int { return toYears(Date().time).toInt() }

    fun age(): String {
        if (!birthday.isNullOrEmpty()){
            return if (deathday.isNullOrEmpty()){
                (currentYear() - birthYear()).toString()
            } else {
                (deathYear() - birthYear()).toString()
            }
        }
        return " --- "
    }

    private fun toYears(milliseconds: Long): String {
        val c = Calendar.getInstance()
        c.timeInMillis = milliseconds
        return c.get(Calendar.YEAR).toString()
    }
}