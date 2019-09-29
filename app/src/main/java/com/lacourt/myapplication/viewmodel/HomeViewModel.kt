package com.lacourt.myapplication.ui.home

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.AppConstants.DATE_ASC
import com.lacourt.myapplication.AppConstants.DATE_DESC
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.indlingresource.IdlingResoureManager
import com.lacourt.myapplication.model.Genre
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.network.Apifactory
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var context: Context
    val movieDao =
        AppDatabase.getDatabase(application)!!.MovieDao() //Not sure if it should be here.

    private val moviesDescending: LiveData<PagedList<Movie>> =
        movieDao.dateDesc().toLiveData(pageSize = 50)
    private val moviesAscending: LiveData<PagedList<Movie>> =
        movieDao.dateAsc().toLiveData(pageSize = 50)

    private var currentOrder = DATE_DESC

    private val dbMovies = movieDao.dateDesc().toLiveData(pageSize = 50)
    val movies = MediatorLiveData<PagedList<Movie>>()

    /*Remember:
     1. that the returned list cannot be mutable
     2. the mutable livedata should be private(check in the video again)*/
    init {
        Log.d(
            "testorder",
            "----------------------------------- HomeViewModel init{...} called --------------------------------\n"
        )
        context = application.applicationContext

        movies.addSource(moviesDescending) { result ->
            if (currentOrder == DATE_DESC) {
                Log.d("testorder", "addSource(moviesDescending)")
                result?.let { movies.value = it }
            }
        }

        movies.addSource(moviesAscending) { result ->
            if (currentOrder == DATE_ASC) {
                Log.d("testorder", "addSource(moviesAscending)")
                result.let { movies.value = it }
            }
        }

//        Log.d("testorder", "init: list = ${moviePagedList?.value?.size}, dbCount = ${movieDao.getCount()}")

        val count = movieDao.getCount()
        if (count < 100) {
            if (count > 0)
                movieDao.deleteAll()

            FetchData().execute()
        }
    }

    fun rearrengeMovies(order: String) = when (order) {
        DATE_ASC -> moviesAscending.value?.let { movies.value = it }
        else -> moviesDescending.value?.let { movies.value = it }

    }.also { currentOrder = order }

    fun getMoviesList(): LiveData<PagedList<Movie>> {
        return movieDao.dateDesc().toLiveData(pageSize = 50)
    }

    inner class FetchData() : AsyncTask<Void, Void, Array<out Void?>>() {
        var genresList: ArrayList<Genre>? = null

        override fun doInBackground(vararg params: Void?): Array<out Void?> {
            Log.d("testorder", "doInbackground called.\n")

            IdlingResoureManager.getIdlingResource().setIdleState(true)
            fetchGenres()
            fetchMovies()
            return params
        }

        override fun onPostExecute(result: Array<out Void?>?) {
            super.onPostExecute(result)
            IdlingResoureManager.getIdlingResource().setIdleState(false)
        }

        fun fetchGenres() {
            try {
                var api = Apifactory.tmdbApi.getGenres()
                var response = api.execute()
                var genreResponse = response.body()
                genresList = genreResponse!!.genres as ArrayList<Genre>

            } catch (e: IOException) {
                networkErrorToast()
            }
        }

        fun fetchMovies() {
            Log.d("testorder", "fetchMovies() called")
            for (i in 1..5) {
                Log.d("testorder", "--------------------------PAGE-${i}--------------------------")
                try {
                    val response = Apifactory.tmdbApi.getMovies(AppConstants.LANGUAGE, i).execute()
                    var movies = response.body()!!.results

                    movies.forEach { movie ->
                        val movieWithGenre = addGenreForEachMovie(movie)
                        Log.d(
                            "testorder",
                            "inserting ${movieWithGenre.title}, id=${movieWithGenre.id}"
                        )
                        movieDao.insert(movieWithGenre)
                    }

                } catch (e: IOException) {
                    networkErrorToast()
                }
            }

        }

        fun addGenreForEachMovie(movie: Movie): Movie {
            movie.genre_ids!!.forEach { id ->
                genresList!!.forEach { genre ->
                    if (genre.id.toString().equals(id)) {
                        movie.genres = ArrayList()
                        movie.genres!!.add(genre.name)
                    }
                }
            }
            return movie
        }

        fun networkErrorToast() {
            Toast.makeText(
                context,
                "Network failure :(",
                Toast.LENGTH_SHORT
            ).show()
        }
/*
        lateinit var target: Target

        private fun saveImages(movie: Movie) {
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w185/${movie.poster_path}")
                .into(target)

            target = object: Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                   encodeBitmapAndSaveToDatabase(bitmap)
                }

            }
        }

        private fun encodeBitmapAndSaveToDatabase(bitmap: Bitmap?) {
            val baos = ByteArrayOutputStream()
            if(bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                var encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

            }
        }
        */
    }


    fun getMovieById(id: Int): Movie? {
       return movieDao.getMovieById(id)
    }

    fun getTestString(): String = "Just a test"
}
