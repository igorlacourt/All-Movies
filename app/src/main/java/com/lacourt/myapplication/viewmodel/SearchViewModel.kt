package com.lacourt.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.repository.SearchRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor() : ViewModel() {

    private val repository = SearchRepository()

    var searchResult:LiveData<ArrayList<MovieDTO>> = repository.searchResult as LiveData<ArrayList<MovieDTO>>

    fun searchMovie(title: String){
        repository.searchMovie(title)
    }
}
