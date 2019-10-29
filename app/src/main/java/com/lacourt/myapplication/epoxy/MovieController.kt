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
    }


    var topTrendingMovie: Details? = null
    var trendingMovies: List<DbMovieDTO>? = null
    var trendingTv: List<DbMovieDTO>? = null
    var topRatedMovies: List<DbMovieDTO>? = null
    var topRatedTv: List<DbMovieDTO>? = null
    var upcomingMovies: List<DbMovieDTO>? = null
    var popularMovies: List<DbMovieDTO>? = null


    fun submitTrendingMovies(newList: List<DbMovieDTO>) {
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
        /*
        val allTrendingModelList = ArrayList<MovieListModel_>()

        trendingAll?.forEach { movie ->
            allTrendingModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in all trending called")
                        onItemClick.onItemClick((onItemClick as Context).getString(R.string.details_type_movie), movie.id)
                    }
            )
        }
        val latestTvModelList = ArrayList<MovieListModel_>()
        latestTv?.forEach { movie ->
            latestTvModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in all trending called")
                        onItemClick.onItemClick(movie.id)
                    }
            )
        }
        val trendingTvModelList = ArrayList<MovieListModel_>()
        trendingTv?.forEach { movie ->
            trendingTvModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in all trending called")
                        onItemClick.onItemClick((onItemClick as Context).getString(R.string.details_type_tv), movie.id)
                    }
            )
        }
        */
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

//        HeaderModel_()
//            .id(1)
//            .header("Trending")
//            .addTo(this)
//        CarouselModel_()
//            .id("trendingAll")
//            .models(allTrendingModelList)
//            .addTo(this)

//        HeaderModel_()
//            .id(2)
//            .header("Latest tv shows")
//            .addTo(this)
//        CarouselModel_()
//            .id("latestTv")
//            .models(latestTvModelList)
//            .addTo(this)

//        HeaderModel_()
//            .id(3)
//            .header("Trending shows")
//            .addTo(this)
//        CarouselModel_()
//            .id("trendingTv")
//            .models(topRatedTvModelList)
//            .addTo(this)

        var genresArrayList: ArrayList<GenreXDTO> = ArrayList()
        topTrendingMovie?.genres?.forEach {
            genresArrayList.add(it)
        }



        TopTrendingMovieModel_(context)
            .id(5)
            .backdropPath(topTrendingMovie?.backdrop_path)
            .genres(genresArrayList)
            .clickListener { model, parentView, clickedView, position ->
                Log.d("clicklog", "onCreateView popular movies called")
                topTrendingMovie?.id?.let { callDetailsFragment(it) }
            }
            .addTo(this)

        HeaderModel_()
            .id(5)
            .header("Trending")
            .addTo(this)
        CarouselModel_()
            .id("trendingMovies")
            .models(trendingMoviesModelList)
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

        HeaderModel_()
            .id(4)
            .header("Critically acclaimed movies")
            .addTo(this)
        CarouselModel_()
            .id("topRatedMovies")
            .models(topRatedMoviesModelList)
            .addTo(this)
    }

    private fun callDetailsFragment(id: Int){
        if (id != 0) {
            onItemClick.onItemClick(id)
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
        }
    }

}