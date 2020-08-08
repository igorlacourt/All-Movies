package com.movies.allmovies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movies.allmovies.database.AppDatabase
import com.movies.allmovies.domainmappers.toDomainMovieList
import com.movies.allmovies.domainmappers.toMovieList
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.dto.MovieDTO

class MyListViewModel(application: Application) : AndroidViewModel(application) {
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()
    private val _myList = MutableLiveData<List<MovieDTO>>()
    var myList: LiveData<List<MovieDTO>> = _myList

    init {
        getList()
    }

    fun getList() {
        _myList.value = myListDao?.all()?.toMovieList()
    }
}