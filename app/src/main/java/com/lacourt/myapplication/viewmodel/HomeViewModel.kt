package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lacourt.myapplication.domainMappers.not_used_interfaces.Mapper
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.repository.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application){

    private var homeRepository: HomeRepository? = null
    var movies: LiveData<PagedList<DbMovieDTO>>? = null
    var popularMovies: LiveData<List<DbMovieDTO>>? = null

    init {
        Log.d("callstest", "homeViewModel init called.\n")
        homeRepository = HomeRepository(application)
        movies = homeRepository?.movies as LiveData<PagedList<DbMovieDTO>>
        popularMovies = homeRepository?.popularMovies as LiveData<List<DbMovieDTO>>
    }

    fun rearrengeMovies(order: String) {
        homeRepository?.rearrengeMovies(order)
    }
}