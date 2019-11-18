package com.lacourt.myapplication.epoxy

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.lacourt.myapplication.domainMappers.toMyListItem
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.domainmodel.DomainMovie
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.network.Error
import com.lacourt.myapplication.ui.OnItemClick
import com.lacourt.myapplication.openYoutube
import com.lacourt.myapplication.viewmodel.HomeViewModel

class MovieController(
    private val context: Context?,
    private val onItemClick: OnItemClick,
    private val viewModel: HomeViewModel
) : EpoxyController() {

    init {
        Log.d("clicklog", "initializing movieController")
        Log.d("genreslog", "MovieController, init called")
    }

    var topTrendingMovie: Details? = null

    var listsOfMovies: List<Collection<DomainMovie>?>? = null
    var error: Error? = null

    var isInDatabase: Boolean = false

    var trendingMoviesModelList: ArrayList<MovieListModel_>? = null
    var upcomingMovieModelList: ArrayList<MovieListModel_>? = null
    var popularMovieModelList: ArrayList<MovieListModel_>? = null
    var topRatedMoviesModelList: ArrayList<MovieListModel_>? = null

    var lastDrawedCarousel: CarouselModel_? = null

    var isMyListPressed = false

    fun submitListsOfMovies(newListsOfMovies: List<Collection<DomainMovie>?>?, error: Error?) {
        Log.d("errorBoolean", "submitListOfMovies, error = $error")
        Log.d("refresh", "HomeFragment, tsubmitListsOfMovies, error = ${error?.cd}")
        Log.d(
            "refresh",
            "HomeFragment, tsubmitListsOfMovies, list.size = ${newListsOfMovies?.size}"
        )

        listsOfMovies = newListsOfMovies
        Log.d("listsLog", "MovieController, listsOfMovies.size = ${listsOfMovies?.size}")
    }

    fun submitTopTrendingMovie(newMovie: Details?, error: Error?) {
        topTrendingMovie = newMovie
        Log.d("refresh", "HomeFragment, submitTopTrendingMovie, error = ${error?.cd}")
        Log.d("refresh", "HomeFragment, submitTopTrendingMovie, list.size = ${newMovie?.title}")
        this.error = error
        requestModelBuild()
    }
    var topTrendingMovieModel: TopTrendingMovieModel_? = null
    fun submitIsInDatabase(newIsInDatabase: Boolean) {
        isInDatabase = newIsInDatabase
        requestModelBuild()
        Log.d(
            "mylistclick",
            "MovieController, submitIsInDatabase, newIsInDatabase = $newIsInDatabase"
        )
    }

    override fun buildModels() {
        Log.d("genreslog", "MovieController, buildModels called")
        Log.d("errorBoolean", "buildModels, error = ${this.error}")
        Log.d("refresh", "buildModels, error = ${this.error}")
        Log.d("mylistclick", "MovieController, buildModels called")

       buildCarousels()

        if (error != null) {
            drawErrorScreen()
        } else {
            drawTopTrendingMovie()
            drawCarousels()
            Log.d("errorBoolean", "buildModels, CarouselModels created")
        }
    }

    private fun buildCarousels() {
        trendingMoviesModelList = ArrayList<MovieListModel_>()
        listsOfMovies?.get(0)?.forEach { movie ->
            trendingMoviesModelList?.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        movie.id?.let { callDetailsFragment(it) }
                    }
            )
        }
        upcomingMovieModelList = ArrayList<MovieListModel_>()
        listsOfMovies?.get(1)?.forEach { movie ->
            upcomingMovieModelList?.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in upcoming movies called")
                        movie.id?.let { callDetailsFragment(it) }
                    }
            )
        }
        popularMovieModelList = ArrayList<MovieListModel_>()
        listsOfMovies?.get(2)?.forEach { movie ->
            popularMovieModelList?.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView popular movies called")
                        movie.id?.let { callDetailsFragment(it) }
                    }
            )
        }
        topRatedMoviesModelList = ArrayList<MovieListModel_>()
        listsOfMovies?.get(3)?.forEach { movie ->
            topRatedMoviesModelList?.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in all trending called")
                        movie.id?.let { callDetailsFragment(it) }
                    }
            )
        }
    }

    private fun drawCarousels() {
        Log.d("errorBoolean", "buildModels, if(error)  = ${this.error}")

        HeaderModel_()
            .id(2)
            .header("Trending")
            .addTo(this)

        trendingMoviesModelList?.let {
            CarouselModel_()
                .id("trendingMovies")
                .models(it)
                .addTo(this)
        }

        HeaderModel_()
            .id(3)
            .header("Upcoming movies")
            .addTo(this)
        upcomingMovieModelList?.let {
            CarouselModel_()
                .id("upcomingMovies")
                .models(it)
                .addTo(this)
        }

        HeaderModel_()
            .id(4)
            .header("Popular movies")
            .addTo(this)
        popularMovieModelList?.let {
            CarouselModel_()
                .id("popularMovies")
                .models(it)
                .addTo(this)
        }

        HeaderModel_()
            .id(5)
            .header("Critically acclaimed movies")
            .addTo(this)
        topRatedMoviesModelList?.let {
            CarouselModel_()
                .id("topRatedMovies")
                .models(it)
                ?.addTo(this)

        }

    }

    private fun drawTopTrendingMovie() {

        if (topTrendingMovie != null) {
            if (topTrendingMovie!!.genres != null && topTrendingMovie!!.title != null) {
                topTrendingMovieModel = TopTrendingMovieModel_(context, isInDatabase)
                    .id("topTrendingMovie")
                    .backdropPath(topTrendingMovie?.backdrop_path)
                    .genresList(topTrendingMovie!!.genres!!)
                    .title(topTrendingMovie!!.title!!)
                    .trailerClickListener { model, parentView, clickedView, position ->
                        topTrendingMovie?.openYoutube(context)
                    }
                    .myListClickListener { model, parentView, clickedView, position ->
                        isMyListPressed = true
                        if (isInDatabase) {
                            Log.d(
                                "mylistclick",
                                "MovieController, myListClickListener, isInDatabase = $isInDatabase"
                            )
                            topTrendingMovie?.id?.let { viewModel.delete(it) }

                        } else {
                            Log.d(
                                "mylistclick",
                                "MovieController, myListClickListener, isInDatabase = $isInDatabase"
                            )
                            topTrendingMovie!!.toMyListItem()?.let { viewModel.insert(it) }
                        }

                    }
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView popular movies called")
                        topTrendingMovie?.id?.let { callDetailsFragment(it) }
                    }

                topTrendingMovieModel?.addTo(this)
            }

        }
    }

    private fun drawErrorScreen() {
        val message = errorMessage()
        Log.d("errorBoolean", "buildModels, if(error)  = ${this.error}")
        ErrorHomeModel_()
            .id(1)
            .message(message)
            .clickListener { model, parentView, clickedView, position ->
                viewModel.refresh()
            }
            .addTo(this)
        Log.d("errorBoolean", "buildModels, ErrorHomeModel created")
    }

    private fun errorMessage() = when (error!!.cd) {
        408 -> "\"Timeout. Your internet may be too slow. Check your connection and try again\""
        99 -> "\"Error. Check your connection and try again\""
        else -> "\"Error. Please, try again latter\""
    }

    private fun callDetailsFragment(id: Int) {
        if (id != 0) {
            onItemClick.onItemClick(id)
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT)
                .show()
        }
    }

}


