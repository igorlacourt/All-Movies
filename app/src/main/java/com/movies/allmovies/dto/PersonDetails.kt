package com.movies.allmovies.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*


data class PersonDetails(
    val id: Int?,
    val name: String?,
    val biography: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
    val popularity: String?,
    @SerializedName("place_of_birth")
    val placeOfBirth: String?,
    val gender: Int?,
    val birthday: String?,
    val deathday: String?,
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