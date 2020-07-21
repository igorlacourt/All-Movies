package com.movies.allmovies.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.Apifactory
import com.movies.allmovies.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor() : ViewModel() {
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
          }
    }
}