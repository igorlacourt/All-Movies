package com.lacourt.myapplication.model.domainmodel

import com.lacourt.myapplication.model.Movie

data class DataMovie(
    val networkMovie: Movie,
    val isFavourite: Boolean
)