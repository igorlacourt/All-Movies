package com.lacourt.myapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.model.Movie

class SearchRepository(application: Application) {
    var searchResult: MutableLiveData<Movie> = MutableLiveData()
    val movieDao = AppDatabase.getDatabase(application)!!.MovieDao()

    fun searchMovie(title:String) : LiveData<List<Movie>> {
        return movieDao.searchMovie(title)
    }
}