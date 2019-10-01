package com.lacourt.myapplication.viewmodel

import android.annotation.SuppressLint
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
import com.lacourt.myapplication.model.GenreResponse
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.MovieResponse
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.network.Apifactory.tmdbApi
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
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

        movieDao.deleteAll()
        rxJava2()

        val count = movieDao.getCount()
        //TODO mudar para 100 de novo
//        if (count < 20) {
//            if (count > 0)
//                movieDao.deleteAll()
//
//            rxJava2()
////            FetchData().execute()
//        }
    }

    fun rearrengeMovies(order: String) = when (order) {
        DATE_ASC -> moviesAscending.value?.let { movies.value = it }
        else -> moviesDescending.value?.let { movies.value = it }

    }.also { currentOrder = order }

    fun getMoviesList(): LiveData<PagedList<Movie>> {
        return movieDao.dateDesc().toLiveData(pageSize = 50)
    }

    var moviesObservable: ArrayList<Movie>? = null

    /*
    fun rxJava() {
        tmdbApi.getMoviesObservable(AppConstants.LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { movieResponse ->
                //                moviesObservable = movieResponse.results
                Observable.fromIterable(movieResponse.results)
                    .subscribeOn(Schedulers.io())
            }
            .flatMap { movie ->
                movieDao.insert(movie)
                Log.d("rxlog", "movieDao.insert")
                tmdbApi.getGenresObservable()
                    .subscribeOn(Schedulers.io())
                    .map { genresResponse ->
                        val genres = genresResponse.genres
                        Thread.sleep(5000)
                        for (i in 0..genres.size - 1) {
                            movie.genre_ids?.forEach { id ->
                                if (genres.get(i).id == id.toInt()) {
                                    movie.genres?.add(genres.get(i).name)
                                }
                            }
                        }
                        movie
                    }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Movie> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(movie: Movie) {
                    Log.d("rxlog", "movieDao.update")
                    movieDao.update(movie)
                    if (currentOrder == DATE_ASC)
                        movies.value = movieDao?.dateAsc().toLiveData(pageSize = 50)
                    else
                        movieDao.dateDesc().toLiveData(pageSize = 50)
//                    updateMovie(movie)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

            })

    }
*/
    fun genresRequest(): Observable<GenreResponse> = tmdbApi.getGenresObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun moviesRequest(): Observable<MovieResponse> =
        tmdbApi.getMoviesObservable(AppConstants.LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    @SuppressLint("CheckResult")
    fun rxJava2() {
        genresRequest()
            .flatMap { genresResponse ->
                var genres = genresResponse.genres as ArrayList<Genre>
                moviesRequest()
                    .flatMap { movieResponse ->
                        Observable.fromIterable(movieResponse.results)
                            .subscribeOn(Schedulers.io())
                    }
                    .map { movie ->
                        var updatedMovie = addGenreForEachMovie2(movie, genres)
                        movieDao.insert(updatedMovie)
                        movie
                    }
            }
            .subscribe(object : Observer<Movie> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(movie: Movie) {
                }

                override fun onError(e: Throwable) {
                    networkErrorToast()
                }

                override fun onComplete() {

                }

            })

    }
    fun addGenreForEachMovie2(movie: Movie, genresList: ArrayList<Genre>): Movie {
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

    fun updateMovie(movie: Movie) {
        if (moviesObservable != null)
            moviesObservable?.set(moviesObservable?.indexOf(movie)!!, movie)
    }

    fun networkErrorToast() {
        Toast.makeText(
            context,
            "Network failure :(",
            Toast.LENGTH_SHORT
        ).show()
    }

//    inner class FetchData() : AsyncTask<Void, Void, Array<out Void?>>() {
//        var genresList: ArrayList<Genre>? = null
//
//        override fun doInBackground(vararg params: Void?): Array<out Void?> {
//            Log.d("testorder", "doInbackground called.\n")
//
//            IdlingResoureManager.getIdlingResource().setIdleState(true)
//            fetchGenres()
//            fetchMovies()
//            return params
//        }
//
//        override fun onPostExecute(result: Array<out Void?>?) {
//            super.onPostExecute(result)
//            IdlingResoureManager.getIdlingResource().setIdleState(false)
//        }
//
//        fun fetchGenres() {
//            try {
//                var api = Apifactory.tmdbApi.getGenres()
//                var response = api.execute()
//                var genreResponse = response.body()
//                genresList = genreResponse!!.genres as ArrayList<Genre>
//
//            } catch (e: IOException) {
//                networkErrorToast()
//            }
//        }
//
//        fun fetchMovies() {
//            Log.d("testorder", "fetchMovies() called")
//            for (i in 1..5) {
//                Log.d("testorder", "--------------------------PAGE-${i}--------------------------")
//                try {
//                    val response = Apifactory.tmdbApi.getMovies(AppConstants.LANGUAGE, i).execute()
//                    var movies = response.body()!!.results
//
//                    movies.forEach { movie ->
//                        val movieWithGenre = addGenreForEachMovie(movie)
//                        Log.d(
//                            "testorder",
//                            "inserting ${movieWithGenre.title}, id=${movieWithGenre.id}"
//                        )
//                        movieDao.insert(movieWithGenre)
//                    }
//
//                } catch (e: IOException) {
//                    networkErrorToast()
//                }
//            }
//
//        }
//
//        fun addGenreForEachMovie(movie: Movie): Movie {
//            movie.genre_ids!!.forEach { id ->
//                genresList!!.forEach { genre ->
//                    if (genre.id.toString().equals(id)) {
//                        movie.genres = ArrayList()
//                        movie.genres!!.add(genre.name)
//                    }
//                }
//            }
//            return movie
//        }
//
//
///*
//        lateinit var target: Target
//
//        private fun saveImages(movie: Movie) {
//            Picasso.get()
//                .load("https://image.tmdb.org/t/p/w185/${movie.poster_path}")
//                .into(target)
//
//            target = object: Target {
//                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//
//                }
//
//                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//
//                }
//
//                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                   encodeBitmapAndSaveToDatabase(bitmap)
//                }
//
//            }
//        }
//
//        private fun encodeBitmapAndSaveToDatabase(bitmap: Bitmap?) {
//            val baos = ByteArrayOutputStream()
//            if(bitmap != null) {
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//                var encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
//
//            }
//        }
//        */
//    }


    fun getMovieById(id: Int): Movie? {
        return movieDao.getMovieById(id)
    }

    fun getTestString(): String = "Just a test"
}

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
    return map {
        if (block(it)) newValue else it
    }
}