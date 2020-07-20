package com.movies.allmovies.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.datasource.HomeDataSource
import com.movies.allmovies.domainmappers.MapperFunctions
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.network.Apifactory.tmdbApi
import com.movies.allmovies.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeDataSource: HomeDataSource, val context: Context) : ViewModel(){
    private val _topTrendingMovie: MutableLiveData<Details>? = MutableLiveData()
    val topTrendingMovie: LiveData<Details>? = _topTrendingMovie

    private val _listsOfMovies: MutableLiveData<List<Collection<DomainMovie>>?> = MutableLiveData()
    val listsOfMovies: LiveData<List<Collection<DomainMovie>>?> = _listsOfMovies

    private val _isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    val isInDatabase: LiveData<Boolean> = _isInDatabase

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    init {
        isLoading.value = true
        Log.d("isld", "init, isLoading.value = ${isLoading.value}")

        getListOfMovies()
    }

    private fun getListOfMovies() {
        viewModelScope.launch{
            homeDataSource.getListsOfMovies{ result ->
                when(result) {
                    is HomeResult.Success -> {
                        Log.d("searchlog", "searchMovie, SearchResult.Success")
                        _listsOfMovies.postValue(result.movies)
                        getTopMovie(result.movies[0].elementAt(0).id)
                    }
                    is HomeResult.ApiError -> {
                        Log.d("searchlog", "searchMovie, SearchResult.ApiError")
                        isLoading.postValue(false)
                    }
                    is HomeResult.ServerError -> {
                        Log.d("searchlog", "searchMovie, SearchResult.ServerError")
                        isLoading.postValue(false)
                    }

                }
            }
        }
    }

    private fun getTopMovie(id: Int?) {
        viewModelScope.launch {
            try {
                val response = tmdbApi.getDetails(id, AppConstants.LANGUAGE)
                when (response) {
                    is NetworkResponse.Success -> {
                        _topTrendingMovie?.postValue(MapperFunctions.toDetails(response.body))
                    }
                    is NetworkResponse.ApiError -> Log.d("getTopMovie", "ApiError ${response.body.message}")
                    is NetworkResponse.NetworkError -> Log.d("getTopMovie", "NetworkError")
                    is NetworkResponse.UnknownError -> Log.d("getTopMovie", "UnknownError")
                }
            } catch (e: Exception) {
                Log.d("parallelRequest", e.message)
                throw e
            }
        }
    }

    fun isTopMovieInDatabase(id: Int){
        _isInDatabase.value = homeDataSource.isTopMovieInDatabase(id)
        isLoading.value = false
        Log.d("isld", "isTopMovieInDatabase, isLoading.value = ${isLoading.value}")
    }

    fun refresh(){
        homeDataSource.refresh()
    }

    fun insert(myListItem: MyListItem) {
        homeDataSource.insert(myListItem)
        _isInDatabase.value = true
    }

    fun delete(id: Int){
        homeDataSource.delete(id)
        _isInDatabase.value = false
    }

}

sealed class HomeResult {
    class Success(val movies: ArrayList<Collection<DomainMovie>>) : HomeResult()
    class ApiError(val statusCode: Int) : HomeResult()
    object ServerError : HomeResult()
}