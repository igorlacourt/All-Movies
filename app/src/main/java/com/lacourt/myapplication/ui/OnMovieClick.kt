package com.lacourt.myapplication.ui

import com.lacourt.myapplication.model.dbMovie.DbMovie

interface OnMovieClick {
    fun onMovieClick(movie: DbMovie)
}