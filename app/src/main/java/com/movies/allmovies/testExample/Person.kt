package com.movies.allmovies.testExample

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period

data class Person(val firstName: String, val lastName: String, val birthDate: LocalDate?) {

    val age
        @RequiresApi(Build.VERSION_CODES.O)
        get() = Period.between(this.birthDate, LocalDate.now()).years

    fun getFirstString(): String = "First"

    fun getSecondString(): String = "Second"
}