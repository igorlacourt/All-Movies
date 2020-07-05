package com.movies.allmovies.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.movies.allmovies.AppConstants
import com.movies.allmovies.database.AppDatabase
import com.movies.allmovies.deleteById
import com.movies.allmovies.domainMappers.MapperFunctions
import com.movies.allmovies.domainMappers.toDomainMovie
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.*
import com.movies.allmovies.idlingresource.IdlingResourceManager
import com.movies.allmovies.insertItem
import com.movies.allmovies.isInDatabase
import com.movies.allmovies.network.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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
        IdlingResourceManager.getIdlingResource().setIdleState(isIdleNow = false)



//        fetchMoviesLists()
        Log.d("callstest", "repository called")
        movieDao?.deleteAll()
    }

    fun requestLists() {

    }

    fun refresh() {
        Log.d("refresh", "HomeRepository, refresh()")
//        fetchMoviesLists()
    }

    @SuppressLint("CheckResult")
     fun fetchMoviesLists() {
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


//                removeRepeated(list3 as ArrayList)


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
            .doOnComplete {  }
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

    private fun removeRepeated(popular: ArrayList<DomainMovie>) {
        for(i in 0 until popular.size) {
            popular.forEach {upcoming ->
                if(popular.elementAt(i).id == upcoming.id)
                    popular.removeAt(i)
            }
        }
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
                Apifactory.tmdbApi.getDetails(id, AppConstants.LANGUAGE),
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


    @SuppressLint("CheckResult")
    fun justForTesting() : Disposable {
        isLoading.value = true
        Log.d("refresh", "HomeRepository, fetchMoviesLists()")
        val tmdbApi = Apifactory.tmdbApi
        val trendinMoviesObservale = tmdbApi.getTrendingMovies(AppConstants.LANGUAGE, 1)
        val upcomingMoviesObservale = tmdbApi.getUpcomingMovies(AppConstants.LANGUAGE, 1)
        val popularMoviesObservale = tmdbApi.getPopularMovies(AppConstants.LANGUAGE, 1)
        val topRatedMoviesObservable = tmdbApi.getTopRatedMovies(AppConstants.LANGUAGE, 1)

       return Observable.zip(
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


//                removeRepeated(list3 as ArrayList)


                var resultList = ArrayList<Collection<DomainMovie>>()
                resultList.add(list1)
                resultList.add(list2)
                resultList.add(list3)
                resultList.add(list4)


                Log.d("refresh", "HomeRepository, resultList.size = ${resultList.size}")
                Log.d("listsLog", "HomeRepository, resultList.size = ${resultList.size}")

                listsOfMovies.postValue(Resource.success(resultList))

//                fetchTopImageDetails(list1.elementAt(0).id)

            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {

            }
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

}

