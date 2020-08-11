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

class HomeViewModel @Inject constructor(val context: Context, private val tmdbApi: TmdbApi, private val homeDataSource: HomeDataSource, @IoDispatcher private val ioDispatcher: CoroutineDispatcher) : ViewModel() {
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
        getListOfMovies()
    }

    fun getListOfMovies() {
        showErrorScreen(false)
        try {
            viewModelScope.launch(ioDispatcher) {
                //The scope bellow is in a background thread called from the data source
                //Because of this, we have to call postValue instead of value for filling the livedatas
                homeDataSource.getListsOfMovies(ioDispatcher) { result ->
                    when (result) {
                        is NetworkResponse.Success -> {
                            _listsOfMovies.postValue(result.body)
                            getTopMovie(result.body[0][0].id)
                        }
                        is NetworkResponse.ApiError -> {
                            showErrorScreen(show = true, message = AppConstants.API_ERROR_MESSAGE)
                        }
                        is NetworkResponse.NetworkError -> {
                            showErrorScreen(
                                show = true,
                                message = AppConstants.NETWORK_ERROR_MESSAGE
                            )
                        }
                        is NetworkResponse.UnknownError -> {
                            showErrorScreen(
                                show = true,
                                message = AppConstants.UNKNOWN_ERROR_MESSAGE
                            )
                        }
                    }
                }
            }
        } catch (exception: Exception){
            throw exception
        }
    }

    private fun getTopMovie(id: Int?) {
        try {
            viewModelScope.launch(ioDispatcher) {
                val response = tmdbApi.getDetails(id, AppConstants.LANGUAGE)
                when (response) {
                    is NetworkResponse.Success -> {
                        _topTrendingMovie?.postValue(response.body.toDetails())
                        showLoadingScreen(false)
                        Log.d("homelog", "getTopMovie")
                        Log.d("homelog", "isLoading = ${isLoading.value}")
                    }
                    is NetworkResponse.ApiError -> {
                        showErrorScreen(show = true, message = AppConstants.API_ERROR_MESSAGE)
                    }
                    is NetworkResponse.NetworkError -> {
                        showErrorScreen(show = true, message = AppConstants.NETWORK_ERROR_MESSAGE)
                    }
                    is NetworkResponse.UnknownError -> {
                        showErrorScreen(show = true, message = AppConstants.UNKNOWN_ERROR_MESSAGE)
                    }
                }
            }
        } catch (exception: Exception){
            throw exception
        }

    }

    fun isTopMovieInDatabase(id: Int){
        _isInDatabase.value = homeDataSource.isTopMovieInDatabase(id)
        isLoading.value = false
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

    private fun showErrorScreen(show: Boolean, message: String? = null) {
        _errorMessage.postValue(message)
        _errorScreenVisibility.postValue(show)
        _isLoading.postValue(!show)
    }

    private fun showLoadingScreen(show: Boolean) {
        _isLoading.postValue(show)
    }

}