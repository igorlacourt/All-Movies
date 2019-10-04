package com.lacourt.myapplication.repository

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.model.Genre
import com.lacourt.myapplication.model.GenreResponse
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.MovieResponse
import com.lacourt.myapplication.network.Apifactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeRepository(val application: Application   ) {

    private var currentOrder = AppConstants.DATE_DESC

    val movieDao =
        AppDatabase.getDatabase(application)!!.MovieDao() //Not sure if it should be here.

    private val moviesDescending: LiveData<PagedList<Movie>> =
        movieDao.dateDesc().toLiveData(pageSize = 50)

    private val moviesAscending: LiveData<PagedList<Movie>> =
        movieDao.dateAsc().toLiveData(pageSize = 50)

    val movies = MediatorLiveData<PagedList<Movie>>()

    /*Remember:
     1. that the returned list cannot be mutable
     2. the mutable livedata should be private(check in the video again)*/
    init {
        Log.d(
            "testorder",
            "----------------------------------- HomeViewModel init{...} called --------------------------------\n"
        )

        movies.addSource(moviesDescending) { result ->
            if (currentOrder == AppConstants.DATE_DESC) {
                Log.d("testorder", "addSource(moviesDescending)")
                result?.let { movies.value = it }
            }
        }

        movies.addSource(moviesAscending) { result ->
            if (currentOrder == AppConstants.DATE_ASC) {
                Log.d("testorder", "addSource(moviesAscending)")
                result.let { movies.value = it }
            }
        }

        movieDao.deleteAll()
        fetchMovies()
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
        AppConstants.DATE_ASC -> moviesAscending.value?.let { movies.value = it }
        else -> moviesDescending.value?.let { movies.value = it }
    }.also { currentOrder = order }

    fun genresRequest(): Observable<GenreResponse> = Apifactory.tmdbApi.getGenresObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun moviesRequest(page: Int): Observable<MovieResponse> =
        Apifactory.tmdbApi.getMoviesObservable(AppConstants.LANGUAGE, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    @SuppressLint("CheckResult")
    fun fetchMovies() {
        genresRequest()
            .flatMap { genresResponse ->
                var genres = genresResponse.genres as ArrayList<Genre>
                Observable.range(1, 10)
                    .flatMap { page ->
                        moviesRequest(page)
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

    fun networkErrorToast() {
        Toast.makeText(
            application.applicationContext,
            "Network failure :(",
            Toast.LENGTH_SHORT
        ).show()
    }

}

