package com.movies.allmovies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.Resource
import com.movies.allmovies.repository.SearchRepositoryImpl
import com.movies.allmovies.repository.SearchResult
import com.movies.allmovies.ui.search.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(val searchRepository: SearchRepository) : ViewModel() {
    var searchResult: MutableLiveData<List<MovieDTO>> = MutableLiveData()

    fun searchMovie(title: String){
          viewModelScope.launch {
              val resource = searchRepository.requestMovie(title)
              when(resource.status) {
                  Resource.Status.SUCCESS -> {
                      searchResult.value = resource.data?.results
                  }
                  Resource.Status.ERROR -> {
                    Log.d("searchlog", "searchMovie, Error")
                  }
              }
          }

//        searchRepository.searchMovie(title) { result: SearchResult ->
//            when(result) {
//                is SearchResult.Success -> {
//                    Log.d("searchlog", "searchMovie, SearchResult.Success")
//                    searchResult.value = result.movies
//                }
//                is SearchResult.ApiError -> {
//                    Log.d("searchlog", "searchMovie, SearchResult.ApiError")
//                }
//                is SearchResult.ServerError -> {
//                    Log.d("searchlog", "searchMovie, SearchResult.ServerError")
//                }
//            }
//        }
    }
}