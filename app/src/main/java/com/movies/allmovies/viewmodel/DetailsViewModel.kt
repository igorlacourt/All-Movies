package com.movies.allmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.database.AppDatabase
import com.movies.allmovies.domainMappers.MapperFunctions
import com.movies.allmovies.domainMappers.toDomainMovie
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.network.Apifactory.tmdbApi
import com.movies.allmovies.repository.NetworkResponse
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application){
    val app : Application = application

    private val _movie: MutableLiveData<Details> = MutableLiveData()
    val movie: LiveData<Details> = _movie

    private val _recommendedMovies: MutableLiveData<Collection<DomainMovie>> = MutableLiveData()
    val recommendedMovies: LiveData<Collection<DomainMovie>> = _recommendedMovies

    private val _isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    val isInDatabase: LiveData<Boolean> = _isInDatabase

    private val myListDao =
        AppDatabase.getDatabase(app)?.MyListDao()

    fun isInDatabase(id: Int?) : Boolean{
        return myListDao?.exists(id) ?: false
    }

    fun addToList() {
        if (isInDatabase.value == false) {
            if (movie.value?.id != null) {
                movie.value?.let { insert(MapperFunctions.toMyListItem(it)) }
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

    fun getDetails(id: Int) {
        viewModelScope.launch {
            val response = tmdbApi.getDetails(id, AppConstants.VIDEOS_AND_CASTS)
            when (response) {
                is NetworkResponse.Success -> {
                    val details = MapperFunctions.toDetails(response.body)
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
                    _recommendedMovies.value = response.body.toDomainMovie()
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

    private fun filterResultsList() {
//        val list = ArrayList<MovieDTO>()
//        list.addAll(t.results)
//        if(list.size  < 3){
//            getSimilar(checkedId)
//        } else {
//            if (list.size == 20) {
//                val last = t.results.size - 1
//                val beforeLast = t.results.size - 2
//                list.removeAt(last)
//                list.removeAt(beforeLast)
//            }
//            recommendedMovies.value =
//                Resource.success(t.toDomainMovie() as List)
//        }
    }

}