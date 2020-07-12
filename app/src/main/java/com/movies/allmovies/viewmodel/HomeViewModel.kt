package com.movies.allmovies.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.domainMappers.MapperFunctions
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.network.Apifactory.tmdbApi
import com.movies.allmovies.network.new_network.HomeResult
import com.movies.allmovies.network.new_network.NetworkResponse
import com.movies.allmovies.repository.HomeDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeDataSource: HomeDataSource, val context: Context) : ViewModel(){
    var topTrendingMovie: MutableLiveData<Details>? = MutableLiveData()
    var listsOfMovies: MutableLiveData<List<Collection<DomainMovie>>?> = MutableLiveData()

    var isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isLoading.value = true
        getListOfMovies()
    }

    private fun getListOfMovies() {
        viewModelScope.launch{
            homeDataSource.getListsOfMovies{ result ->
                when(result) {
                    is HomeResult.Success -> {
                        Log.d("searchlog", "searchMovie, SearchResult.Success")
                        listsOfMovies.postValue(result.movies)
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
                        topTrendingMovie?.postValue(MapperFunctions.toDetails(response.body))
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
        isInDatabase.value = homeDataSource.isTopMovieInDatabase(id)
        isLoading.value = false
    }

    fun refresh(){
        homeDataSource.refresh()
    }

    fun insert(myListItem: MyListItem) {
        homeDataSource.insert(myListItem)
        isInDatabase.value = true
    }

    fun delete(id: Int){
        homeDataSource.delete(id)
        isInDatabase.value = false
    }

}