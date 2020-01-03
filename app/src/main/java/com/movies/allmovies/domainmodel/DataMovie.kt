package com.movies.allmovies.domainmodel

import com.movies.allmovies.dto.MovieDTO

data class DataMovie(
    val networkMovieDTO: MovieDTO,
    val isFavourite: Boolean
)