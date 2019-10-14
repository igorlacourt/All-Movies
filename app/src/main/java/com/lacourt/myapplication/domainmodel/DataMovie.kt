package com.lacourt.myapplication.domainmodel

import com.lacourt.myapplication.dto.MovieDTO

data class DataMovie(
    val networkMovieDTO: MovieDTO,
    val isFavourite: Boolean
)