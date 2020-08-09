package com.movies.allmovies.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.di.IoDispatcher
import com.movies.allmovies.domainmappers.toDetails
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import com.movies.allmovies.repository.HomeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(val context: Context, private val tmdbApi: TmdbApi, private val homeDataSource: HomeDataSource, @IoDispatcher private val ioDispatcher: CoroutineDispatcher) : ViewModel(){
    private val _topTrendingMovie: MutableLiveData<Details>? = MutableLiveData()
    val topTrendingMovie: LiveData<Details>? = _topTrendingMovie

    private val _listsOfMovies: MutableLiveData<List<List<MovieDTO>>?> = MutableLiveData()
    val listsOfMovies: LiveData<List<List<MovieDTO>>?> = _listsOfMovies

    private val _isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    val isInDatabase: LiveData<Boolean> = _isInDatabase

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private var _errorScreenVisibility = MutableLiveData<Boolean>(false)
    var errorScreenVisibility: LiveData<Boolean> = _errorScreenVisibility

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    init {
        isLoading.value = true
        Log.d("isld", "init, isLoading.value = ${isLoading.value}")

        getListOfMovies()
    }

    fun getListOfMovies() {
        viewModelScope.launch(ioDispatcher){
            homeDataSource.getListsOfMovies(ioDispatcher){ result ->
                when(result) {
                    is NetworkResponse.Success -> {
                        Log.d("searchlog", "searchMovie, SearchResult.Success")
                        _listsOfMovies.postValue(result.body)
                        getTopMovie(result.body[0].elementAt(0).id)
                    }
                    is NetworkResponse.ApiError -> {
                        Log.d("searchlog", "searchMovie, SearchResult.ApiError")
                        _errorMessage.value = AppConstants.API_ERROR_MESSAGE
                        showErrorScreen(true)
                        isLoading.postValue(false)
                    }
                    is NetworkResponse.NetworkError -> {
                        Log.d("searchlog", "searchMovie, SearchResult.ServerError")
                        _errorMessage.value = AppConstants.API_ERROR_MESSAGE
                        showErrorScreen(true)
                        isLoading.postValue(false)
                    }
                    is NetworkResponse.UnknownError -> {
                        _errorMessage.value = AppConstants.UNKNOWN_ERROR_MESSAGE
                        showErrorScreen(true)
                    }
                }
            }
        }
    }

    private fun showErrorScreen(shouldShow: Boolean) {
        _errorScreenVisibility.value = shouldShow
    }

    private fun getTopMovie(id: Int?) {
        viewModelScope.launch {
            try {
                val response = tmdbApi.getDetails(id, AppConstants.LANGUAGE)
                when (response) {
                    is NetworkResponse.Success -> {
                        _topTrendingMovie?.postValue(response.body.toDetails())
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
    class Success(val movies: ArrayList<List<MovieDTO>>) : HomeResult()
    class ApiError(val statusCode: Int) : HomeResult()
    object ServerError : HomeResult()
}