package com.lacourt.myapplication.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.deleteById
import com.lacourt.myapplication.domainMappers.MapperFunctions
import com.lacourt.myapplication.domainMappers.toDomainMovie
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.domainmodel.DomainMovie
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.*
import com.lacourt.myapplication.insertItem
import com.lacourt.myapplication.isInDatabase
import com.lacourt.myapplication.network.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HomeRepository(private val application: Application) : NetworkCallback<Details> {
    //    private val config = PagedList.Config.Builder()
//        .setInitialLoadSizeHint(50)
//        .setPageSize(50)
//        .setEnablePlaceholders(false)
//        .build()
    private val context: Context = application

    private val movieDao =
        AppDatabase.getDatabase(application)?.MovieDao()

    val topTrendingMovie = MutableLiveData<Resource<Details>>()
    val listsOfMovies = MutableLiveData<Resource<List<Collection<DomainMovie>>>>()
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()

    var isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    /*Remember:
     1. that the returned list cannot be mutable
     2. the mutable livedata should be private(check in the video again)*/
    init {
        fetchMoviesLists()
        Log.d("callstest", "repository called")
        movieDao?.deleteAll()
    }

    fun refresh() {
        Log.d("refresh", "HomeRepository, refresh()")
        fetchMoviesLists()
    }

    @SuppressLint("CheckResult")
    private fun fetchMoviesLists() {
        isLoading.value = true
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

                val list1 = trendingMoviesResponse.toDomainMovie()
                val list2 = upcomingMoviesResponse.toDomainMovie()
                val list3 = popularMoviesResponse.toDomainMovie()
                val list4 = topRatedMoviesResponse.toDomainMovie()

                var resultList = ArrayList<Collection<DomainMovie>>()
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
                isLoading.value = false
            })
    }

    fun insert(myListItem: MyListItem) {
        Log.d("log_is_inserted", "HomeRepository, insert() called")
        Log.d("mylistclick", "HomeRepository, insert() called, myListItem.id = ${myListItem.id}")
        myListDao?.insertItem(context, myListItem, isInDatabase)
    }

    fun delete(id: Int?) {
        Log.d("log_is_inserted", "HomeRepository, delete() called")
        Log.d("mylistclick", "HomeRepository, delete() called, id = $id")
        myListDao.deleteById(context, id, isInDatabase)
    }

    private fun fetchTopImageDetails(id: Int?) {
        if (id != null)
            NetworkCall<DetailsDTO, Details>().makeCall(
                Apifactory.tmdbApi.getDetails(id, AppConstants.VIDEOS_AND_CASTS),
                this,
                MapperFunctions::toDetails
            )
    }

    override fun networkCallResult(callback: Resource<Details>) {
        topTrendingMovie.value = callback
        isInDatabase(callback.data?.id)
        isLoading.value = false
    }

    fun isInDatabase(id: Int?) {
        id?.let { myListDao.isInDatabase(id, isInDatabase) }
    }

    fun networkErrorToast() {
        Toast.makeText(
            application.applicationContext,
            "Network failure :(",
            Toast.LENGTH_SHORT
        ).show()
    }


}

