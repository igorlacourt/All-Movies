package com.lacourt.myapplication.repository

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.MapperFunctions
import com.lacourt.myapplication.domainMappers.mapToDomain
import com.lacourt.myapplication.domainMappers.not_used_interfaces.Mapper
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.dto.*
import com.lacourt.myapplication.network.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.functions.Function4
import io.reactivex.functions.Function6
import io.reactivex.functions.Function7
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HomeRepository(private val application: Application) : NetworkCallback<Details> {
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

    val topTrendingMovie = MutableLiveData<Resource<Details>>()
    val trendingMovies = MutableLiveData<Resource<List<DbMovieDTO>>>()

    val listsOfMovies = MutableLiveData<Resource<List<Collection<DbMovieDTO>>>>()

    /*Remember:
     1. that the returned list cannot be mutable
     2. the mutable livedata should be private(check in the video again)*/
    init {
        fetchMoviesLists()
        Log.d("callstest", "repository called")
        /*
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
*/
        movieDao.deleteAll()
//        fetchMovies()
//        val count = movieDao.getCount()
        //TODO mudar para 100 de novo
//        if (count < 20) {
//            if (count > 0)
//                movieDao.deleteAll()
//        }
    }

    fun refresh(){
        Log.d("refresh", "HomeRepository, refresh()")
        fetchMoviesLists()
    }

    @SuppressLint("CheckResult")
    private fun fetchMoviesLists() {
        Log.d("refresh", "HomeRepository, fetchMoviesLists()")
        val tmdbApi = Apifactory.tmdbApi
        val trendinMoviesObservale = tmdbApi.getTrendingMovies(AppConstants.LANGUAGE, 1)
        val upcomingMoviesObservale = tmdbApi.getUpcomingMovies(AppConstants.LANGUAGE, 1)
        val popularMoviesObservale = tmdbApi.getPopularMovies(AppConstants.LANGUAGE, 1)
        val topRatedMoviesObservable = tmdbApi.getTopRatedMovies(AppConstants.LANGUAGE, 1)

        Observable.zip(
            trendinMoviesObservale.subscribeOn(Schedulers.io()),
            upcomingMoviesObservale.subscribeOn(Schedulers.io()),
            popularMoviesObservale.subscribeOn(Schedulers.io()),
            topRatedMoviesObservable.subscribeOn(Schedulers.io()),

            Function4 { trendingMoviesResponse: MovieResponseDTO,
                        upcomingMoviesResponse: MovieResponseDTO,
                        popularMoviesResponse: MovieResponseDTO,
                        topRatedMoviesResponse: MovieResponseDTO ->

                val list1 = trendingMoviesResponse.mapToDomain()
                val list2 = upcomingMoviesResponse.mapToDomain()
                val list3 = popularMoviesResponse.mapToDomain()
                val list4 = topRatedMoviesResponse.mapToDomain()

                var resultList = ArrayList<Collection<DbMovieDTO>>()
                resultList.add(list1)
                resultList.add(list2)
                resultList.add(list3)
                resultList.add(list4)

                Log.d("refresh", "HomeRepository, resultList.size = ${resultList.size}")
                Log.d("listsLog", "HomeRepository, resultList.size = ${resultList.size}")

                listsOfMovies.postValue(Resource.success(resultList))

                fetchTopImageDetails(list1.elementAt(0).id)

            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { error ->
                when (error) {
                    is SocketTimeoutException -> {
                        Log.d("errorBoolean", "HomeRepository, is SocketTimeoutException")
                        Log.d("listsLog", "HomeRepository, is SocketTimeoutException")
                        topTrendingMovie.postValue(
                            Resource.error(Error(408, "SocketTimeoutException"))
                        )
                    }
                    is UnknownHostException -> {
                        Log.d("errorBoolean", "HomeRepository, is UnknownHostException")
                        Log.d("listsLog", "HomeRepository, is UnknownHostException")
                        topTrendingMovie.postValue(
                            Resource.error(Error(99, "UnknownHostException"))
                        )
                    }
                    is HttpException -> {
                        Log.d("errorBoolean", "HomeRepository, is HttpException")
                        Log.d("listsLog", "HomeRepository, is HttpException")
                        topTrendingMovie.postValue(
                            Resource.error(Error(error.code(), error.message()))
                        )
                    }
                    else -> {
                        Log.d("errorBoolean", "HomeRepository, is Another Error")
                        Log.d("listsLog", "HomeRepository, is Another Error")
                        topTrendingMovie.postValue(
                            Resource.error(
                                Error(0, error.message)
                            )
                        )
                    }

                }
            })
    }

//    fun <T> Observable<T>.mapNetworkErrors(): Observable<T> =
//        this.doOnError { error ->
//            when (error) {
//                is SocketTimeoutException -> Observable.error(NoNetworkException(error))
//                is UnknownHostException -> Observable.error(ServerUnreachableException(error))
//                is HttpException -> Observable.error(HttpCallFailureException(error))
//                else -> Observable.error(error)
//            }
//        }

    private fun fetchTopImageDetails(id: Int) {
        NetworkCall<DetailsDTO, Details>().makeCall(
            Apifactory.tmdbApi.getDetails(id, AppConstants.VIDEOS),
            this,
            MapperFunctions::toDetails
        )
    }

    override fun networkCallResult(callback: Resource<Details>) {
        topTrendingMovie.value = callback
    }

    /*
    fun rearrengeMovies(order: String) = when (order) {
        AppConstants.DATE_ASC -> moviesAscending.value?.let { popularMovies.value = it }
        else -> moviesDescending.value?.let { popularMovies.value = it }
    }.also { currentOrder = order }
    */

//    fun moviesRequest(page: Int): Observable<MovieResponseDTO>? =
//        Apifactory.tmdbApi.getPopularMovies(AppConstants.LANGUAGE, page)
//            .subscribeOn(Schedulers.io())
//            ?.observeOn(AndroidSchedulers.mainThread())


    @SuppressLint("CheckResult")
    fun fetchMovies() {
//        Observable.range(1, 10)
//            .flatMap { page ->
//                moviesRequest(page)
//                    ?.map {
//                        MapperFunctions.toListOfDbMovieDTO(it.results)
//                    }
//                    ?.flatMap {
//                        Observable.fromIterable(it)
//                            .subscribeOn(Schedulers.io())
//                    }
//            }
//            .doOnNext {
//                Log.d("rxjavalog", "doOnNext called, id = ${it.id}")
//                movieDao.insert(it)
//            }
//            .doOnComplete {
//                Log.d("rxjavalog", "doOnComplete called")
//            }
//            .doOnError { networkErrorToast() }
//            .subscribe()
    }

    fun networkErrorToast() {
        Toast.makeText(
            application.applicationContext,
            "Network failure :(",
            Toast.LENGTH_SHORT
        ).show()
    }


}

