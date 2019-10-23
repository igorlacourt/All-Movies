package com.lacourt.myapplication.epoxy

import com.airbnb.epoxy.EpoxyController
import com.lacourt.myapplication.dto.DbMovieDTO

class MovieController : EpoxyController() {

    var movies: List<DbMovieDTO>? = null
    var popularMovies: List<DbMovieDTO>? = null

    fun submitMovie(newMovieList: List<DbMovieDTO>) {
        movies = newMovieList
        requestModelBuild()
    }

    fun submitPopularMovies(newPopularMovies: List<DbMovieDTO>){
        popularMovies = newPopularMovies
        requestModelBuild()
    }

    override fun buildModels() {

        movies?.forEach { movie ->
            MovieModel_()
                .id(movie.id)
                .movieItem(movie)
                .addTo(this)
        }

        popularMovies?.forEach { movie ->
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