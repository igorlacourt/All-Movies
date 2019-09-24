package com.lacourt.myapplication.ui

import com.lacourt.myapplication.model.Movie

interface OnMovieClick {
    fun onMovieClick(movie: Movie)
}