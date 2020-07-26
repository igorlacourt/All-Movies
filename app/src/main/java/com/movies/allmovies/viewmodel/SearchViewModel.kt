package com.movies.allmovies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.di.MainDispatcher
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val tmdbApi: TmdbApi, @MainDispatcher val mainDispatcher: CoroutineDispatcher) : ViewModel() {
    private var _searchResult = MutableLiveData<List<MovieDTO>>()
    var searchResult: LiveData<List<MovieDTO>> = _searchResult

    private var _apiErrorResult = MutableLiveData<Error>()
    var apiErrorResult: LiveData<Error> = _apiErrorResult

    private var _networkErrorResult = MutableLiveData<IOException>()
    var networkErrorResult: LiveData<IOException> = _networkErrorResult

    private var _unknownErrorResult = MutableLiveData<Throwable>()
    var unknownErrorResult: LiveData<Throwable> = _unknownErrorResult

    val TAG = "calltest"
    fun searchMovie(title: String){
          viewModelScope.launch(mainDispatcher) {
              val response = tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, title, false)
              when (response) {
                  is NetworkResponse.Success -> {
                      Log.d(TAG, "Success ${response.body.results[0].title}")
                      _searchResult.value = response.body.results
                  }
                  is NetworkResponse.ApiError -> { _apiErrorResult.value = response.body }
                  is NetworkResponse.NetworkError -> { _networkErrorResult.value = response.error }
                  is NetworkResponse.UnknownError -> { _unknownErrorResult.value = response.error }
              }
          }
    }
}