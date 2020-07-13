package com.movies.allmovies.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.Apifactory
import com.movies.allmovies.repository.NetworkResponse
import com.movies.allmovies.ui.search.SearchRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(val searchRepository: SearchRepository) : ViewModel() {
    var searchResult: MutableLiveData<List<MovieDTO>> = MutableLiveData()

    val TAG = "calltest"
    fun searchMovie(title: String){
          viewModelScope.launch {
              val response = Apifactory.tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, title, false)
              when (response) {
                  is NetworkResponse.Success -> {
                      Log.d(TAG, "Success ${response.body.results[0].title}")
                      searchResult.value = response.body.results
                  }
                  is NetworkResponse.ApiError -> Log.d(TAG, "ApiError ${response.body.message}")
                  is NetworkResponse.NetworkError -> Log.d(TAG, "NetworkError")
                  is NetworkResponse.UnknownError -> Log.d(TAG, "UnknownError")
              }
//              val resource = searchRepository.requestMovie(title)
//              when(resource.status) {
//                  Resource.Status.SUCCESS -> {
//                      searchResult.value = resource.data?.results
//                  }
//                  Resource.Status.ERROR -> {
//                    Log.d("searchlog", "searchMovie, Error")
//                  }
//                  Resource.Status.LOADING -> TODO()
//              }
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