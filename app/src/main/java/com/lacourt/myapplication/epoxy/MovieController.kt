package com.lacourt.myapplication.epoxy

import android.util.Log
import android.view.View
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.OnModelClickListener
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.ui.OnMovieClick

class MovieController(
    private val onMovieClick: OnMovieClick
) : EpoxyController() {

    init {
        Log.d("clicklog", "initializing movieController")
    }
    //    var movies: List<DbMovieDTO>? = null
    var allTrending: List<DbMovieDTO>? = null
    var upcomingMovies: List<DbMovieDTO>? = null
    var popularMovies: List<DbMovieDTO>? = null

//    fun submitMovie(newMovieList: List<DbMovieDTO>) {
//        movies = newMovieList
//        requestModelBuild()
//    }

    fun submitAllTrending(newAllTrending: List<DbMovieDTO>) {
        allTrending = newAllTrending
        requestModelBuild()
    }

    fun submitUpcomingMovies(newUpcoming: List<DbMovieDTO>) {
        upcomingMovies = newUpcoming
        requestModelBuild()
    }

    fun submitPopularMovies(newPopularMovies: List<DbMovieDTO>) {
        popularMovies = newPopularMovies
        requestModelBuild()
    }

    override fun buildModels() {
        val allTrendingModelList = ArrayList<MovieListModel_>()
        allTrending?.forEach { movie ->
            allTrendingModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in all trending called")
                        onMovieClick.onMovieClick(movie.id)
                    }
            )
        }
        val upcomingMovieModelList = ArrayList<MovieListModel_>()
        upcomingMovies?.forEach { movie ->
            upcomingMovieModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in upcoming movies called")
                        onMovieClick.onMovieClick(movie.id)
                    }

//                    .cardClickListener { model, parentView, clickedView, position ->
//                        Log.d("onclicklog", "onClickCalled")
//                        onMovieClick.onMovieClick(movie.id)
//                    }
            )
        }
        val popularMovieModelList = ArrayList<MovieListModel_>()
        popularMovies?.forEach { movie ->
            popularMovieModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView popular movies called")
                        onMovieClick.onMovieClick(movie.id)
                    }
            )
        }
        HeaderModel_()
            .id(1)
            .header("Trending")
            .addTo(this)

        CarouselModel_()
            .id("carousel-1")
            .models(allTrendingModelList)
            .addTo(this)

        HeaderModel_()
            .id(2)
            .header("Upcoming movies")
            .addTo(this)

        CarouselModel_()
            .id("carousel-2")
            .models(upcomingMovieModelList)
            .addTo(this)

        HeaderModel_()
            .id(3)
            .header("Popular movies")
            .addTo(this)

        CarouselModel_()
            .id("carousel-3")
            .models(popularMovieModelList)
            .addTo(this)


    }

}