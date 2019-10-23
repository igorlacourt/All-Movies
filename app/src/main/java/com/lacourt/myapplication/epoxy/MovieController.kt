package com.lacourt.myapplication.epoxy

import com.airbnb.epoxy.EpoxyController
import com.lacourt.myapplication.dto.DbMovieDTO

class MovieController : EpoxyController() {

    var movies: List<DbMovieDTO>? = null

    fun setMovie(newMovieList: List<DbMovieDTO>) {
        movies = newMovieList
        requestModelBuild()
    }

    override fun buildModels() {

        movies?.forEach { movie ->
            MovieModel_()
                .id(movie.id)
                .movieItem(movie)
                .addTo(this)
        }

//        movies?.let {movieList ->
//            MovieModel_()
//                .movieItem(movie)
//                .addTo(this)
//        }
    }

}