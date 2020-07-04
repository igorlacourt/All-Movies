package com.movies.allmovies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.repository.SearchRepositoryImpl
import com.movies.allmovies.repository.SearchResult
import com.movies.allmovies.ui.search.SearchRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(val searchRepository: SearchRepository) : ViewModel() {
    var searchResult: MutableLiveData<ArrayList<MovieDTO>> = MutableLiveData<ArrayList<MovieDTO>>()// = (searchRepository as SearchRepositoryImpl).searchResult// as LiveData<ArrayList<MovieDTO>>

    fun searchMovie(title: String){
        searchRepository.searchMovie(title) { result: SearchResult ->
            when(result) {
                is SearchResult.Success -> {
                    Log.d("searchlog", "searchMovie, SearchResult.Success")
                    searchResult.value = result.movies
                }
                is SearchResult.ApiError -> {
                    Log.d("searchlog", "searchMovie, SearchResult.ApiError")
                }
                is SearchResult.ServerError -> {
                    Log.d("searchlog", "searchMovie, SearchResult.ServerError")
                }
            }
        }
    }
}