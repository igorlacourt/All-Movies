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
import com.lacourt.myapplication.domainMappers.Mapper
import com.lacourt.myapplication.model.dto.Genre
import com.lacourt.myapplication.model.dto.GenreResponse
import com.lacourt.myapplication.model.dto.Movie
import com.lacourt.myapplication.model.dto.MovieResponse
import com.lacourt.myapplication.model.dbmodel.DbMovie
import com.lacourt.myapplication.network.Apifactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository(private val application: Application, private val movieDataMapper: Mapper<Movie, DbMovie>) {
    private val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(50)
        .setPageSize(50)
        .setEnablePlaceholders(false)
        .build()

    private var currentOrder = AppConstants.DATE_DESC

    private val movieDao =
        AppDatabase.getDatabase(application)!!.MovieDao() //Not sure if it should be here.

    private val moviesDescending: LiveData<PagedList<DbMovie>> =
        movieDao.dateDesc().toLiveData(config)

    private val moviesAscending: LiveData<PagedList<DbMovie>> =
        movieDao.dateAsc().toLiveData(config)

    val movies = MediatorLiveData<PagedList<DbMovie>>()

    /*Remember:
     1. that the returned list cannot be mutable
     2. the mutable livedata should be private(check in the video again)*/
    init {
        Log.d("callstest", "repository called")
        movies.addSource(moviesDescending) { result ->
            if (currentOrder == AppConstants.DATE_DESC) {
                Log.d("callstest", "addSource(moviesDescending)")
                result?.let { movies.value = it }
            }
        }

        movies.addSource(moviesAscending) {result ->
            if (currentOrder == AppConstants.DATE_ASC) {
                Log.d("callstest", "addSource(moviesAscending)")
                result.let { movies.value = it }
            }
        }

        movieDao.deleteAll()
        fetchMovies()
//        val count = movieDao.getCount()
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

    fun genresRequest(): Observable<GenreResponse>? = Apifactory.tmdbApi?.getGenresObservable()
        ?.subscribeOn(Schedulers.io())
        ?.observeOn(AndroidSchedulers.mainThread())

    fun moviesRequest(page: Int): Observable<MovieResponse>? =
        Apifactory.tmdbApi?.getMoviesObservable(AppConstants.LANGUAGE, page)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

    fun loadMovies(page: Int){
        Apifactory.tmdbApi.getMovies(AppConstants.LANGUAGE, page).enqueue(object :
            Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if(response.isSuccessful)
                    mapMovies(response.body()?.results as List<Movie>)
            }
        })
    }

    private fun mapMovies(networkMoviesList: List<Movie>): List<DbMovie> {
        return networkMoviesList.map {
           movieDataMapper.map(it)
        }
    }

    @SuppressLint("CheckResult")
    fun fetchMovies() {
        Observable.range(1, 10)
            .flatMap { page ->
                moviesRequest(page)
                    ?.map {
                        mapMovies(it.results)
                    }
                    ?.flatMap {
                        Observable.fromIterable(it)
                            .subscribeOn(Schedulers.io())
                    }
            }
            .subscribe(object : Observer<DbMovie> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(movie: DbMovie) {
                    movieDao.insert(movie)
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

