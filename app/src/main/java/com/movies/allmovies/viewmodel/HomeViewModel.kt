package com.movies.allmovies.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.network.Resource
import com.movies.allmovies.repository.HomeRepository
import javax.inject.Inject

class HomeViewModel (application: Application) : AndroidViewModel(application){
    var app = application

    private var repository: HomeRepository? = null
    var topTrendingMovie: LiveData<Resource<Details>>? = null
    var listsOfMovies: LiveData<Resource<List<Collection<DomainMovie>>>?>? = null

    var isInDatabase: LiveData<Boolean>? = null
    var isLoading: LiveData<Boolean>? = null

    init {
//        Log.d("callstest", "homeViewModel init called.\n")
        repository = HomeRepository(application)
        topTrendingMovie = repository?.topTrendingMovie

        listsOfMovies = repository?.listsOfMovies

        isInDatabase = repository?.isInDatabase
        isLoading = repository?.isLoading
//        Log.d("listsLog", "HomeViewModel, resultList.size = ${listsOfMovies?.value?.data?.size}")
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