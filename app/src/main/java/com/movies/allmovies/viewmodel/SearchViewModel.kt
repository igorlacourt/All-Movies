package com.movies.allmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.repository.SearchRepositoryImpl
import com.movies.allmovies.ui.search.SearchRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(val searchRepository: SearchRepository) : ViewModel() {
    var searchResult:LiveData<ArrayList<MovieDTO>> = (searchRepository as SearchRepositoryImpl).searchResult as LiveData<ArrayList<MovieDTO>>
    fun searchMovie(title: String){
        searchRepository.searchMovie(title)
    }
}