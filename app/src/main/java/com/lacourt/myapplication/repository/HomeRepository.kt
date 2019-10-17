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
import com.lacourt.myapplication.domainMappers.MapperFunctions
import com.lacourt.myapplication.domainMappers.not_used_interfaces.Mapper
import com.lacourt.myapplication.dto.GenreDTO
import com.lacourt.myapplication.dto.GenreResponseDTO
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.dto.MovieResponseDTO
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.network.Apifactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository(private val application: Application) {
    private val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(50)
        .setPageSize(50)
        .setEnablePlaceholders(false)
        .build()

    private var currentOrder = AppConstants.DATE_DESC

    private val movieDao =
        AppDatabase.getDatabase(application)!!.MovieDao() //Not sure if it should be here.

    private val moviesDescending: LiveData<PagedList<DbMovieDTO>> =
        movieDao.dateDesc().toLiveData(config)

    private val moviesAscending: LiveData<PagedList<DbMovieDTO>> =
        movieDao.dateAsc().toLiveData(config)

    val movies = MediatorLiveData<PagedList<DbMovieDTO>>()

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
//        fetchMovies()
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

    fun moviesRequest(page: Int): Observable<MovieResponseDTO>? =
        Apifactory.tmdbApi.getMoviesObservable(AppConstants.LANGUAGE, page)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())


    @SuppressLint("CheckResult")
    fun fetchMovies() {
       Observable.range(1, 10)
            .flatMap { page ->
                moviesRequest(page)
                    ?.map {
                        MapperFunctions.toListOfDbMovieDTO(it.results)
                    }
                    ?.flatMap {
                        Observable.fromIterable(it)
                            .subscribeOn(Schedulers.io())
                    }
            }
           .doOnNext {
               Log.d("rxjavalog", "doOnNext called, id = ${it.id}")
               movieDao.insert(it)
           }
           .doOnComplete{
               Log.d("rxjavalog", "doOnComplete called")
           }
           .doOnError { networkErrorToast() }
           .subscribe()
    }

    fun networkErrorToast() {
        Toast.makeText(
            application.applicationContext,
            "Network failure :(",
            Toast.LENGTH_SHORT
        ).show()
    }

}

