package com.lacourt.myapplication.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.repository.SearchRepository

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val query = MutableLiveData<String>()
    private val repository = SearchRepository(application)

    val searchResult: LiveData<List<Movie>> = Transformations.switchMap(
        query,
        ::func
    )

    private fun func(name: String) = repository.searchMovie(name)
    fun searchMovie(name: String) = apply { query.value = name }
}
