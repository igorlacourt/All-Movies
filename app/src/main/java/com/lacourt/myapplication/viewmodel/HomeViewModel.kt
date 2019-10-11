package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lacourt.myapplication.domainMappers.Mapper
import com.lacourt.myapplication.model.dto.Movie
import com.lacourt.myapplication.model.dbmodel.DbMovie
import com.lacourt.myapplication.repository.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application),
    Mapper<Movie, DbMovie> {

    override fun map(input: Movie): DbMovie{
        return with(input){
            DbMovie(id, poster_path, release_date, vote_average)
        }
    }

    private var homeRepository: HomeRepository? = null
    var movies: LiveData<PagedList<DbMovie>>? = null

    init {
        Log.d("callstest", "homeViewModel init called.\n")
        homeRepository = HomeRepository(application, this)
        movies = homeRepository?.movies as LiveData<PagedList<DbMovie>>
    }

    fun rearrengeMovies(order: String) {
        homeRepository?.rearrengeMovies(order)
    }
}