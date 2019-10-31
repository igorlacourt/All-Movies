package com.lacourt.myapplication.epoxy

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.lacourt.myapplication.R
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.dto.GenreXDTO
import com.lacourt.myapplication.ui.OnItemClick

class MovieController(
    private val context: Context?,
    private val onItemClick: OnItemClick
) : EpoxyController() {

    init {
        Log.d("clicklog", "initializing movieController")
        Log.d("genreslog", "MovieController, init called")
    }


    var topTrendingMovie: Details? = null
    var trendingMovies: List<DbMovieDTO>? = null
    var topRatedMovies: List<DbMovieDTO>? = null
    var upcomingMovies: List<DbMovieDTO>? = null
    var popularMovies: List<DbMovieDTO>? = null
    var topRatedTv: List<DbMovieDTO>? = null
    var trendingTv: List<DbMovieDTO>? = null


    fun submitTrendingMovies(newList: List<DbMovieDTO>, error: Boolean) {
        trendingMovies = newList
        requestModelBuild()
    }


    fun submitTopTrendingMovie(newMovie: Details) {
        topTrendingMovie = newMovie
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
        Log.d("genreslog", "MovieController, buildModels called")
        val trendingMoviesModelList = ArrayList<MovieListModel_>()
        trendingMovies?.forEach { movie ->
            trendingMoviesModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        callDetailsFragment(movie.id)
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
                        callDetailsFragment(movie.id)
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
                        callDetailsFragment(movie.id)
                    }
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
                        callDetailsFragment(movie.id)
                    }
            )
        }


        topTrendingMovie?.genres?.let {
            topTrendingMovie?.title?.let { it1 ->
                TopTrendingMovieModel_(context)
                    .id("topTrendingMovie")
                    .backdropPath(topTrendingMovie?.backdrop_path)
                    .genresList(it)
                    .title(it1)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView popular movies called")
                        topTrendingMovie?.id?.let { callDetailsFragment(it) }
                    }
                    .addTo(this)
            }
        }

        HeaderModel_()
            .id(2)
            .header("Trending")
            .addTo(this)
        CarouselModel_()
            .id("trendingMovies")
            .models(trendingMoviesModelList)
            .addTo(this)

        HeaderModel_()
            .id(3)
            .header("Upcoming movies")
            .addTo(this)
        CarouselModel_()
            .id("upcomingMovies")
            .models(upcomingMovieModelList)
            .addTo(this)

        HeaderModel_()
            .id(4)
            .header("Popular movies")
            .addTo(this)
        CarouselModel_()
            .id("popularMovies")
            .models(popularMovieModelList)
            .addTo(this)

        HeaderModel_()
            .id(5)
            .header("Critically acclaimed movies")
            .addTo(this)
        CarouselModel_()
            .id("topRatedMovies")
            .models(topRatedMoviesModelList)
            .addTo(this)
    }

    private fun callDetailsFragment(id: Int) {
        if (id != 0) {
            onItemClick.onItemClick(id)
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
        }
    }

}