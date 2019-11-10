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
    var trendingMovies: List<DbMovieDTO>? = null

    var listsOfMovies: List<Collection<DomainMovie>?>? = null
    var error: Error? = null

    var isInDatabase: Boolean = false

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

    fun submitIsInDatabase(newIsInDatabase: Boolean) {
        isInDatabase = newIsInDatabase
        requestModelBuild()
        Log.d("mylistclick", "MovieController, submitIsInDatabase, newIsInDatabase = $newIsInDatabase")
    }

    fun submitTrendingMovies(newList: List<DbMovieDTO>?, error: Error?) {
        Log.d("errorBoolean", "submitTrendingMovies, error = $error")
        error?.let {
            this.error = it
        }
        newList?.let { trendingMovies = it }
        requestModelBuild()
    }

    override fun buildModels() {
        Log.d("genreslog", "MovieController, buildModels called")
        Log.d("errorBoolean", "buildModels, error = ${this.error}")
        Log.d("refresh", "buildModels, error = ${this.error}")
        Log.d("mylistclick", "MovieController, buildModels called")

        val trendingMoviesModelList = ArrayList<MovieListModel_>()
        listsOfMovies?.get(0)?.forEach { movie ->
            trendingMoviesModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        movie.id?.let { callDetailsFragment(it) }
                    }
            )
        }
        val upcomingMovieModelList = ArrayList<MovieListModel_>()
        listsOfMovies?.get(1)?.forEach { movie ->
            upcomingMovieModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in upcoming movies called")
                        movie.id?.let { callDetailsFragment(it) }
                    }
            )
        }
        val popularMovieModelList = ArrayList<MovieListModel_>()
        listsOfMovies?.get(2)?.forEach { movie ->
            popularMovieModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView popular movies called")
                        movie.id?.let { callDetailsFragment(it) }
                    }
            )
        }
        val topRatedMoviesModelList = ArrayList<MovieListModel_>()
        listsOfMovies?.get(3)?.forEach { movie ->
            topRatedMoviesModelList.add(
                MovieListModel_()
                    .id(movie.id)
                    .mMoviePoster(movie.poster_path)
                    .clickListener { model, parentView, clickedView, position ->
                        Log.d("clicklog", "onCreateView in all trending called")
                        movie.id?.let { callDetailsFragment(it) }
                    }
            )
        }

        if (error != null) {
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
        } else {
            if (topTrendingMovie != null) {
                if (topTrendingMovie!!.genres != null && topTrendingMovie!!.title != null) {
                    val topTrendingMovieModel = TopTrendingMovieModel_(context, isInDatabase)
                        .id("topTrendingMovie")
                        .backdropPath(topTrendingMovie?.backdrop_path)
                        .genresList(topTrendingMovie!!.genres!!)
                        .title(topTrendingMovie!!.title!!)
                        .trailerClickListener { model, parentView, clickedView, position ->
                            topTrendingMovie?.openYoutube(context)
                        }
                        .myListClickListener { model, parentView, clickedView, position ->
                            if (isInDatabase){
                                Log.d("mylistclick", "MovieController, myListClickListener, isInDatabase = $isInDatabase")
                                topTrendingMovie?.id?.let { viewModel.delete(it) }
                            }
                            else{
                                Log.d("mylistclick", "MovieController, myListClickListener, isInDatabase = $isInDatabase")
                                topTrendingMovie!!.toMyListItem()?.let { viewModel.insert(it) }
                            }

                        }
                        .clickListener { model, parentView, clickedView, position ->
                            Log.d("clicklog", "onCreateView popular movies called")
                            topTrendingMovie?.id?.let { callDetailsFragment(it) }
                        }

//                    if (context != null) {
//                        val icon = ImageView(context)
//                        if (isInDatabase) {
//                            icon.setImageDrawable(
//                                ResourcesCompat.getDrawable(
//                                    context.resources,
//                                    R.drawable.ic_check_mark_24dp,
//                                    null
//                                )
//                            )
//                            topTrendingMovieModel.myListIcon?.setImageDrawable(
//                                ResourcesCompat.getDrawable(
//                                    context.resources,
//                                    R.drawable.ic_check_mark_24dp,
//                                    null
//                                )
//                            )
////                            topTrendingMovieModel.myListIcon(icon)
//
//                        } else {
//                            icon.setImageDrawable(
//                                ResourcesCompat.getDrawable(
//                                    context.resources,
//                                    R.drawable.wish_list_btn_24dp,
//                                    null
//                                )
//                            )
//                            topTrendingMovieModel.myListIcon?.setImageDrawable(
//                                ResourcesCompat.getDrawable(
//                                    context.resources,
//                                    R.drawable.wish_list_btn_24dp,
//                                    null
//                                )
//                            )
////                            topTrendingMovieModel.myListIcon(icon)
//
//                        }
//                    }
                    topTrendingMovieModel.addTo(this)
                }

            }



            Log.d("errorBoolean", "buildModels, if(error)  = ${this.error}")
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

            Log.d("errorBoolean", "buildModels, CarouselModels created")
        }
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


