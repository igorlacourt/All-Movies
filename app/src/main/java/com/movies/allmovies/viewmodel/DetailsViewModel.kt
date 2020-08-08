package com.movies.allmovies.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.database.AppDatabase
import com.movies.allmovies.di.MainDispatcher
import com.movies.allmovies.domainmappers.toDetails
import com.movies.allmovies.domainmappers.toMyListItem
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(val context: Context, private val tmdbApi: TmdbApi, @MainDispatcher val mainDispatcher: CoroutineDispatcher) : ViewModel(){

    private val _movie: MutableLiveData<Details> = MutableLiveData()
    val movie: LiveData<Details> = _movie

    private val _recommendedMovies: MutableLiveData<List<MovieDTO>> = MutableLiveData()
    val recommendedMovies: LiveData<List<MovieDTO>> = _recommendedMovies

    private val _isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    val isInDatabase: LiveData<Boolean> = _isInDatabase

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val myListDao =
        AppDatabase.getDatabase(context)?.MyListDao()

    fun getDetails(id: Int) {
        _isLoading.value = true
        viewModelScope.launch(mainDispatcher) {
            val response = tmdbApi.getDetails(id, AppConstants.VIDEOS_AND_CASTS)
            when (response) {
                is NetworkResponse.Success -> {
                    val details = response.body.toDetails()
                    getRecommendations(details.id)
                    _movie.value = details
                    _isInDatabase.postValue(isInDatabase(details.id))
                }
                is NetworkResponse.ApiError -> Log.d(
                    "detailsviewmodel",
                    "ApiError ${response.body.message}"
                )
                is NetworkResponse.NetworkError -> Log.d("detailsviewmodel", "NetworkError")
                is NetworkResponse.UnknownError -> Log.d("detailsviewmodel", "UnknownError")
            }
        }
    }

    private fun getRecommendations(id: Int?) {
        viewModelScope.launch {
            val response = tmdbApi.getRecommendations(id, AppConstants.LANGUAGE, 1)
            when (response) {
                is NetworkResponse.Success -> {
                    _recommendedMovies.value = response.body.results
                    _isLoading.value = false
                }
                is NetworkResponse.ApiError -> Log.d(
                    "detailsviewmodel",
                    "ApiError ${response.body.message}"
                )
                is NetworkResponse.NetworkError -> Log.d("detailsviewmodel", "NetworkError")
                is NetworkResponse.UnknownError -> Log.d("detailsviewmodel", "UnknownError")
            }
        }
    }

    fun isInDatabase(id: Int?) : Boolean{
        return myListDao?.exists(id) ?: false
    }

    fun addToList() {
        if (isInDatabase.value == false) {
            if (movie.value?.id != null) {
                movie.value?.let { insert(it.toMyListItem()) }
            }
        } else {
            movie.value?.id?.let { id -> delete(id) }
        }
    }

    fun insert(myListItem: MyListItem) {
        myListDao?.insert(myListItem)
        _isInDatabase.value = true
    }

    fun delete(id: Int){
        myListDao?.deleteById(id)
        _isInDatabase.value = false
    }

//    private fun filterResultsList(list: Collection<DomainMovie>) {
//        val list = ArrayList<MovieDTO>()
//        list.addAll(t.results)
//
//        if (list.size == 20) {
//            val last = list.size - 1
//            val beforeLast = list.size - 2
//            list.remove()
//            list.removeAt(beforeLast)
//        }
//            _recommendedMovies.value = list
//    }

}