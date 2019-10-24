package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lacourt.myapplication.domainMappers.not_used_interfaces.Mapper
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.repository.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application){

    private var homeRepository: HomeRepository? = null
//    var upcomingMovies: LiveData<PagedList<DbMovieDTO>>? = null
    var upcomingMovies: LiveData<Resource<List<DbMovieDTO>>>? = null
    var popularMovies: LiveData<Resource<List<DbMovieDTO>>>? = null
    var trendingAll: LiveData<Resource<List<DbMovieDTO>>>? = null

    init {
        Log.d("callstest", "homeViewModel init called.\n")
        homeRepository = HomeRepository(application)
//        upcomingMovies = homeRepository?.upcomingMovies as LiveData<PagedList<DbMovieDTO>>
        upcomingMovies = homeRepository?.upcomingMovies
        popularMovies = homeRepository?.popularMovies
        trendingAll = homeRepository?.trendingAll
    }

    fun rearrengeMovies(order: String) {
//        homeRepository?.rearrengeMovies(order)
    }
}