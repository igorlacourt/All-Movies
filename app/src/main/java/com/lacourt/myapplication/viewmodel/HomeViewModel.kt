package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.repository.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application){
    var app = application

    private var repository: HomeRepository? = null
    var topTrendingMovie: LiveData<Resource<Details>>? = null
    var listsOfMovies: LiveData<Resource<List<Collection<DbMovieDTO>>>?>? = null

    var isInDatabase: LiveData<Boolean>? = null

    init {
        Log.d("callstest", "homeViewModel init called.\n")
        repository = HomeRepository(application)
        topTrendingMovie = repository?.topTrendingMovie

        listsOfMovies = repository?.listsOfMovies

        isInDatabase = repository?.isInDatabase
        Log.d("listsLog", "HomeViewModel, resultList.size = ${listsOfMovies?.value?.data?.size}")
    }

    fun refresh(){
        Log.d("refresh", "HomeViewMmodel, refresh()")
        repository?.refresh()
        Log.d("listsLog", "HomeViewModel, resultList.size = ${listsOfMovies?.value?.data?.size}")
    }



}