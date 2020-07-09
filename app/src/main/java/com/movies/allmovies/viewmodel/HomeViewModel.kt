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
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.Resource
import com.movies.allmovies.repository.HomeRepository
import com.movies.allmovies.repository.NetworkResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel (application: Application) : AndroidViewModel(application){
    var app = application

    private var repository: HomeRepository? = null
    var topTrendingMovie: LiveData<Resource<Details>>? = null
    var listsOfMovies: MutableLiveData<Resource<List<Collection<DomainMovie>>>?> = MutableLiveData()

    var isInDatabase: LiveData<Boolean>? = null
    var isLoading: MutableLiveData<Boolean>? = MutableLiveData()

    init {
        repository = HomeRepository(application)

        parallelRequest()

        topTrendingMovie = repository?.topTrendingMovie

        isInDatabase = repository?.isInDatabase
    }

    fun parallelRequest() {
        viewModelScope.launch {
            Log.d("parallelRequest", "parallelRequest(), viewModelScope.launch")
            var trending: MovieResponseDTO
            var upcoming: MovieResponseDTO
            var popular: MovieResponseDTO
            var topRated: MovieResponseDTO

            val tmdbApi = Apifactory.tmdbApi
            try {
                val trendingMoviesResponse = async { tmdbApi.getTrendingMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val upcomingMoviesResponse = async { tmdbApi.getUpcomingMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val popularMoviesResponse = async { tmdbApi.getPopularMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val topRatedMoviesResponse = async { tmdbApi.getTopRatedMoviesSuspend(AppConstants.LANGUAGE, 1) }
                processData(
                    trendingMoviesResponse.await(),
                    upcomingMoviesResponse.await(),
                    popularMoviesResponse.await(),
                    topRatedMoviesResponse.await()
                )
            } catch (exception: Exception) {
                Log.d("parallelRequest", exception.message)
            }
        }
    }

    private fun processData(
        trending: NetworkResponse<MovieResponseDTO, Error>,
        upcoming: NetworkResponse<MovieResponseDTO, Error>,
        popular: NetworkResponse<MovieResponseDTO, Error>,
        topRated: NetworkResponse<MovieResponseDTO, Error>
    ) {
        val list1: Collection<DomainMovie>? = convertResponse(trending)
        val list2 = convertResponse(upcoming)
        val list3 = convertResponse(popular)
        val list4 = convertResponse(topRated)

        val resultList = ArrayList<Collection<DomainMovie>>()
        list1?.let { resultList.add(it) }
        list2?.let { resultList.add(it) }
        list3?.let { resultList.add(it) }
        list4?.let { resultList.add(it) }

        Log.d("parallelRequest", "processData(), resultList.size = ${resultList.size}")

        listsOfMovies.value = if (resultList.size == 4){
            Resource.success(resultList)
        } else {
            Resource.error(Error(0, "error loading at least one of the movie lists"))
        }
        isLoading?.value = false
    }

    private fun convertResponse(trending: NetworkResponse<MovieResponseDTO, Error>): Collection<DomainMovie>? {
        when(trending){
            is NetworkResponse.Success -> {
                return trending.body.toDomainMovie()
            }
            is NetworkResponse.ApiError -> {
                Log.d("TAG", "ApiError ${trending.body}")
                return null
            }
            is NetworkResponse.NetworkError -> {
                Log.d("TAG", "NetworkError")
                return null
            }
            is NetworkResponse.UnknownError -> {
                Log.d("TAG", "UnknownError")
                return null
            }
        }
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