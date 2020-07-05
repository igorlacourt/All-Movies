package com.movies.allmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.domainMappers.toDomainMovie
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Apifactory
import com.movies.allmovies.network.Resource
import com.movies.allmovies.repository.HomeRepository
import com.movies.allmovies.repository.SearchRepositoryImpl
import com.movies.allmovies.ui.search.ServiceResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel (application: Application) : AndroidViewModel(application){
    var app = application

    private var repository: HomeRepository? = null
    var topTrendingMovie: LiveData<Resource<Details>>? = null
    var listsOfMovies: MutableLiveData<Resource<List<Collection<DomainMovie>>>?> = MutableLiveData()

    var isInDatabase: LiveData<Boolean>? = null
    var isLoading: LiveData<Boolean>? = null

    init {
//        Log.d("callstest", "homeViewModel init called.\n")
        repository = HomeRepository(application)



        parallelRequest()

        topTrendingMovie = repository?.topTrendingMovie

//        listsOfMovies = repository?.listsOfMovies

        isInDatabase = repository?.isInDatabase
        isLoading = repository?.isLoading
//        Log.d("listsLog", "HomeViewModel, resultList.size = ${listsOfMovies?.value?.data?.size}")
    }

    fun parallelRequest() {
        viewModelScope.launch {
            Log.d("parallelRequest", "parallelRequest(), viewModelScope.launch")
            val tmdbApi = Apifactory.tmdbApi
            try {
                val trendingMoviesResponse = async { tmdbApi.getTrendingMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val upcomingMoviesResponse = async { tmdbApi.getUpcomingMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val popularMoviesResponse = async { tmdbApi.getPopularMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val topRatedMoviesResponse = async { tmdbApi.getTopRatedMoviesSuspend(AppConstants.LANGUAGE, 1) }
                processData(
                    trendingMoviesResponse.await().body(),
                    upcomingMoviesResponse.await().body(),
                    popularMoviesResponse.await().body(),
                    topRatedMoviesResponse.await().body()
                )
            } catch (exception: Exception) {
                Log.d("parallelRequest", exception.message)
            }
        }
    }

    fun processData(
        trending: MovieResponseDTO?,
        upcoming: MovieResponseDTO?,
        popular: MovieResponseDTO?,
        topRated: MovieResponseDTO?
    ) {
        Log.d("parallelRequest", "processData()")
        val list1 = trending?.toDomainMovie()
        val list2 = upcoming?.toDomainMovie()
        val list3 = popular?.toDomainMovie()
        val list4 = topRated?.toDomainMovie()

        var resultList = ArrayList<Collection<DomainMovie>>()
        list1?.let { resultList.add(it) }
        list2?.let { resultList.add(it) }
        list3?.let { resultList.add(it) }
        list4?.let { resultList.add(it) }

        Log.d("parallelRequest", "processData(), resultList.size = ${resultList.size}")

        listsOfMovies?.value = Resource.success(resultList)
    }

    fun isIndatabase(id: Int?){
        repository?.isInDatabase(id)
    }

    fun refresh(){
//        Log.d("refresh", "HomeViewMmodel, refresh()")
        repository?.refresh()
//        Log.d("listsLog", "HomeViewModel, resultList.size = ${listsOfMovies?.value?.data?.size}")
    }

    fun insert(myListItem: MyListItem) {
        Log.d("log_is_inserted", "HomeViewModel, insert() called")
        repository?.insert(myListItem)
    }

    fun delete(id: Int){
//        Log.d("log_is_inserted", "HomeViewModel, delete() called")
        repository?.delete(id)
    }

}