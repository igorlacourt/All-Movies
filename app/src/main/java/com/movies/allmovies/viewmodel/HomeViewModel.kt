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
import com.movies.allmovies.repository.HomeResult
import com.movies.allmovies.repository.NetworkResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel (application: Application) : AndroidViewModel(application){
    var app = application

    private var repository: HomeRepository? = null
    var topTrendingMovie: LiveData<Resource<Details>>? = null
    var listsOfMovies: MutableLiveData<List<Collection<DomainMovie>>?> = MutableLiveData()

    var isInDatabase: LiveData<Boolean>? = null
    var isLoading: MutableLiveData<Boolean>? = MutableLiveData()

    init {
        repository = HomeRepository(application)

        viewModelScope.launch{
            repository?.parallelRequest{ result ->
                when(result) {
                    is HomeResult.Success -> {
                    Log.d("searchlog", "searchMovie, SearchResult.Success")
                        listsOfMovies.postValue(result.movies)
                        isLoading?.postValue(false)
                    }
                    is HomeResult.ApiError -> {
                      Log.d("searchlog", "searchMovie, SearchResult.ApiError")
                        isLoading?.postValue(false)
                    }
                    is HomeResult.ServerError -> {
                      Log.d("searchlog", "searchMovie, SearchResult.ServerError")
                        isLoading?.postValue(false)
                    }

                }
            }
        }

        topTrendingMovie = repository?.topTrendingMovie

        isInDatabase = repository?.isInDatabase
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