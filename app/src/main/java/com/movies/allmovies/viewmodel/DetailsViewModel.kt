package com.movies.allmovies.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
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
import com.movies.allmovies.dto.CastDTO
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.Apifactory
import com.movies.allmovies.network.Apifactory.tmdbApi
import com.movies.allmovies.repository.DetailsRepository
import com.movies.allmovies.network.Resource
import com.movies.allmovies.repository.NetworkResponse
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application){
    val app : Application = application
    private val repository: DetailsRepository = DetailsRepository(application)

    private val _movie: MutableLiveData<Details> = MutableLiveData()
    val movie: LiveData<Details> = _movie

    private val _recommendedMovies: MutableLiveData<Collection<DomainMovie>> = MutableLiveData()
    val recommendedMovies: LiveData<Collection<DomainMovie>> = _recommendedMovies

    private val _isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    val isInDatabase: LiveData<Boolean> = _isInDatabase

    private val myListDao =
        AppDatabase.getDatabase(app)?.MyListDao()

    fun isInMyList(id: Int?): Boolean {
        if (id == null)
            return false
        return true
    }

    fun isInDatabase(id: Int?) : Boolean{
        return myListDao?.exists(id) ?: false
    }

//    fun getDetails(id: Int) {
//        Log.d("calltest", "fetchDetails called")
//        movie = repository.movie
//        repository.getDetails(id)
//        repository.getRecommendedMovies(id)
//    }

    fun addToList() {
        if (isInDatabase.value == false) {
            Log.d("log_is_inserted", "isInDatabase false")
            if (movie.value?.id != null) {
                movie.value?.let { insert(MapperFunctions.toMyListItem(it)) }
            }
        } else {
            Log.d("log_is_inserted", "isInDatabase true")
            movie.value?.id?.let { id -> delete(id) }
        }
    }


    fun insert(myListItem: MyListItem) {
        Log.d("log_is_inserted", "DetailsViewModel, insert() called")
        myListDao?.insert(myListItem)
        _isInDatabase.value = true
    }

    fun delete(id: Int){
        Log.d("log_is_inserted", "DetailsViewModel, delete() called")
        myListDao?.deleteById(id)
        _isInDatabase.value = false
    }

    fun getDetails(id: Int) {
        viewModelScope.launch {
            val response = tmdbApi.getDetails(id, AppConstants.LANGUAGE)
            when (response) {
                is NetworkResponse.Success -> {
                    Log.d("detailsviewmodel", "Success ${response.body.title}")
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
                    Log.d("detailsviewmodel", "Success ${response.body.results[0].title}")
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