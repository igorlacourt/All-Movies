package com.lacourt.myapplication.model.domainmodel

import com.lacourt.myapplication.model.dto.Movie

data class DataMovie(
    val networkMovie: Movie,
    val isFavourite: Boolean
)