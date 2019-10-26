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
    var trendingAll: List<DbMovieDTO>? = null
    var latestTv: List<DbMovieDTO>? = null
    var trendingTv: List<DbMovieDTO>? = null
    var topRatedMovies: List<DbMovieDTO>? = null
    var topRatedTv: List<DbMovieDTO>? = null
    var upcomingMovies: List<DbMovieDTO>? = null
    var popularMovies: List<DbMovieDTO>? = null

//    fun submitMovie(newMovieList: List<DbMovieDTO>) {
//        movies = newMovieList
//        requestModelBuild()
//    }

    fun submitAllTrending(newList: List<DbMovieDTO>) {
        trendingAll = newList
        requestModelBuild()
    }

    fun submitLatestTv(newList: List<DbMovieDTO>) {
        latestTv = newList
        requestModelBuild()
    }

    fun submitTrendingTv(newList: List<DbMovieDTO>) {
        trendingTv = newList
        requestModelBuild()
    }

    fun submitTopRatedMovies(newList: List<DbMovieDTO>) {
        topRatedMovies = newList
        requestModelBuild()
    }

    fun submitTopRatedTv(newList: List<DbMovieDTO>) {
        topRatedTv = newList
        requestModelBuild()
    }

    fun submitUpcomingMovies(newList: List<DbMovieDTO>) {
        upcomingMovies = newList
        requestModelBuild()
    }

    fun submitPopularMovies(newList: List<DbMovieDTO>) {
        popularMovies = newList
        requestModelBuild()
    }

    override fun buildModels() {
        val allTrendingModelList = ArrayList<MovieListModel_>()
        trendingAll?.forEach { movie ->
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
//        val latestTvModelList = ArrayList<MovieListModel_>()
//        latestTv?.forEach { movie ->
//            latestTvModelList.add(
//                MovieListModel_()
//                    .id(movie.id)
//                    .mMoviePoster(movie.poster_path)
//                    .clickListener { model, parentView, clickedView, position ->
//                        Log.d("clicklog", "onCreateView in all trending called")
//                        onMovieClick.onMovieClick(movie.id)
//                    }
//            )
//        }
        val trendingTvModelList = ArrayList<MovieListModel_>()
        trendingTv?.forEach { movie ->
            trendingTvModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in all trending called")
                        onMovieClick.onMovieClick(movie.id)
                    }
            )
        }
        val topRatedMoviesModelList = ArrayList<MovieListModel_>()
        topRatedMovies?.forEach { movie ->
            topRatedMoviesModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in all trending called")
                        onMovieClick.onMovieClick(movie.id)
                    }
            )
        }
        val topRatedTvModelList = ArrayList<MovieListModel_>()
        topRatedTv?.forEach { movie ->
            topRatedTvModelList.add(
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
            .id("trendingAll")
            .models(allTrendingModelList)
            .addTo(this)

//        HeaderModel_()
//            .id(2)
//            .header("Latest tv shows")
//            .addTo(this)
//        CarouselModel_()
//            .id("latestTv")
//            .models(latestTvModelList)
//            .addTo(this)

        HeaderModel_()
            .id(3)
            .header("Trending shows")
            .addTo(this)
        CarouselModel_()
            .id("trendingTv")
            .models(topRatedTvModelList)
            .addTo(this)

        HeaderModel_()
            .id(4)
            .header("Critically acclaimed movies")
            .addTo(this)
        CarouselModel_()
            .id("topRatedMovies")
            .models(topRatedMoviesModelList)
            .addTo(this)

        HeaderModel_()
            .id(5)
            .header("Critically acclaimed shows")
            .addTo(this)
        CarouselModel_()
            .id("topRatedShows")
            .models(topRatedTvModelList)
            .addTo(this)

        HeaderModel_()
            .id(6)
            .header("Upcoming movies")
            .addTo(this)
        CarouselModel_()
            .id("upcomingMovies")
            .models(upcomingMovieModelList)
            .addTo(this)

        HeaderModel_()
            .id(7)
            .header("Popular movies")
            .addTo(this)
        CarouselModel_()
            .id("popularMovies")
            .models(popularMovieModelList)
            .addTo(this)


    }

}