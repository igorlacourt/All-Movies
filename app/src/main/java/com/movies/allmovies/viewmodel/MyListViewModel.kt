package com.movies.allmovies.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movies.allmovies.database.MyListDao
import com.movies.allmovies.domainmappers.toMovieList
import com.movies.allmovies.dto.MovieDTO
import javax.inject.Inject

class MyListViewModel @Inject constructor(val context: Context, private val myListDao: MyListDao) : ViewModel() {

    private val _myList = MutableLiveData<List<MovieDTO>>()
    var myList: LiveData<List<MovieDTO>> = _myList

    init {
        getList()
    }

    fun getList() {
        _myList.value = myListDao.all().toMovieList()
    }
}