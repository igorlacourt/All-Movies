package com.lacourt.myapplication.domainmodel

import com.lacourt.myapplication.dto.GenreXDTO

data class Details(
    val backdrop_path: String?,
    val genres: List<GenreXDTO>,
    val id: Int,
    val overview: String,
    val poster_path: String,
    var release_date: String,
    val title: String,
    val vote_average: Double
){
    init{
        release_date = release_date.subSequence(0,4).toString()
    }
}
