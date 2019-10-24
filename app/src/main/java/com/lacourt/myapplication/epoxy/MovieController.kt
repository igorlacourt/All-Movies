package com.lacourt.myapplication.epoxy

import android.view.View
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.OnModelClickListener
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.ui.OnMovieClick

class MovieController(private val onMovieClick: OnMovieClick) : EpoxyController() {

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
        val movieModel = ArrayList<MovieListModel_>()
        movies?.forEach { movie ->
            movieModel.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .onClickListener(object :
                        OnModelClickListener<MovieListModel_, MovieListModel.ViewHolder> {
                        override fun onClick(
                            model: MovieListModel_,
                            parentView: MovieListModel.ViewHolder,
                            clickedView: View,
                            position: Int
                        ) {
                            onMovieClick.onMovieClick(movie.id)
                        }
                    })
            )
        }

        val popularMovieModel = ArrayList<MovieListModel_>()
        popularMovies?.forEach { movie ->
            popularMovieModel.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .onClickListener(object :
                        OnModelClickListener<MovieListModel_, MovieListModel.ViewHolder> {
                        override fun onClick(
                            model: MovieListModel_,
                            parentView: MovieListModel.ViewHolder,
                            clickedView: View,
                            position: Int
                        ) {
                            onMovieClick.onMovieClick(movie.id)
                        }
                    })
            )
        }

        HeaderModel_()
            .id(1)
            .header("Upcoming movies")
            .addTo(this)

        CarouselModel_()
            .id("carousel1")
            .models(movieModel)
            .addTo(this)

        HeaderModel_()
            .id(2)
            .header("Popular movies")
            .addTo(this)

        CarouselModel_()
            .id("carousel2")
            .models(popularMovieModel)
            .addTo(this)


    }

}