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
import com.lacourt.myapplication.domainMappers.not_used_interfaces.Mapper
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.dto.*
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.network.NetworkCall
import com.lacourt.myapplication.network.NetworkCallback
import com.lacourt.myapplication.network.Resource
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
import retrofit2.Response

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

    val topRatedMovies = MutableLiveData<Resource<List<DbMovieDTO>>>()
    val trendingMovies = MutableLiveData<Resource<List<DbMovieDTO>>>()
    val upcomingMovies = MutableLiveData<Resource<List<DbMovieDTO>>>()
    val popularMovies = MutableLiveData<Resource<List<DbMovieDTO>>>()
    val topTrendingMovie = MutableLiveData<Resource<Details>>()
//    val topRatedTv = MutableLiveData<Resource<List<DbMovieDTO>>>()
//    val trendingTv = MutableLiveData<Resource<List<DbMovieDTO>>>()

    /*Remember:
     1. that the returned list cannot be mutable
     2. the mutable livedata should be private(check in the video again)*/
    init {

//        val trendingTvObservable = tmdbApi.getTrendingTv(AppConstants.LANGUAGE, 1)
//        val topRatedTvObservable = tmdbApi.getTopRatedTv(AppConstants.LANGUAGE, 1)
//        trendingTvObservable.subscribeOn(Schedulers.io()),
//        topRatedTvObservable.subscribeOn(Schedulers.io()),
//        trendingTvResponse:MovieResponseDTO,
//        topRatedTvResponse:MovieResponseDTO,
//        var map3 = MapperFunctions.movieResponseToDbMovieDTO(trendingTvResponse)
//        var map5 = MapperFunctions.movieResponseToDbMovieDTO(topRatedTvResponse)
//        trendingTv.postValue(Resource.success(map3))
//        topRatedTv.postValue(Resource.success(map5))

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

                var map1 = MapperFunctions.movieResponseToDbMovieDTO(trendingMoviesResponse)
                var map2 = MapperFunctions.movieResponseToDbMovieDTO(upcomingMoviesResponse)
                var map3 = MapperFunctions.movieResponseToDbMovieDTO(popularMoviesResponse)
                var map4 = MapperFunctions.movieResponseToDbMovieDTO(topRatedMoviesResponse)

                trendingMovies.postValue(Resource.success(map1))
                upcomingMovies.postValue(Resource.success(map2))
                popularMovies.postValue(Resource.success(map3))
                topRatedMovies.postValue(Resource.success(map4))

                fetchTopImageDetails(map1[0].id)

            })
            .subscribe()

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

    private fun fetchTopImageDetails(id: Int){
//        Observable.just(Apifactory.tmdbApi.getDetails2(id))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnNext{
//                topTrendingMovie.value = Resource.success(MapperFunctions.toDetails(it))
//            }
        NetworkCall<DetailsDTO, Details>().makeCall(
            Apifactory.tmdbApi.getDetails(id),
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

    fun moviesRequest(page: Int): Observable<MovieResponseDTO>? =
        Apifactory.tmdbApi.getPopularMovies(AppConstants.LANGUAGE, page)
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
            .doOnComplete {
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

