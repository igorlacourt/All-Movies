package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lacourt.myapplication.domainMappers.not_used_interfaces.Mapper
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.repository.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application){
    var app = application

    private var homeRepository: HomeRepository? = null
    var topTrendingMovie: LiveData<Resource<Details>>? = null

    var listsOfMovies: LiveData<Resource<List<Collection<DbMovieDTO>>>?>? = null

    init {
        Log.d("callstest", "homeViewModel init called.\n")
        homeRepository = HomeRepository(application)
        topTrendingMovie = homeRepository?.topTrendingMovie

        listsOfMovies = homeRepository?.listsOfMovies
        Log.d("listsLog", "HomeViewModel, resultList.size = ${listsOfMovies?.value?.data?.size}")
    }

    fun refresh(){
        Log.d("refresh", "HomeViewMmodel, refresh()")
        homeRepository?.refresh()
        Log.d("listsLog", "HomeViewModel, resultList.size = ${listsOfMovies?.value?.data?.size}")
    }

}