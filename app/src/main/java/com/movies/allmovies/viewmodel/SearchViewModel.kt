package com.movies.allmovies.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.allmovies.AppConstants
import com.movies.allmovies.R
import com.movies.allmovies.di.MainDispatcher
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val context: Context, private val tmdbApi: TmdbApi, @MainDispatcher val mainDispatcher: CoroutineDispatcher) : ViewModel() {

    private var _searchResult = MutableLiveData<List<MovieDTO>>()
    var searchResult: LiveData<List<MovieDTO>> = _searchResult

    private var _errorVisibility = MutableLiveData<Boolean>(false)
    var errorVisibility: LiveData<Boolean> = _errorVisibility

    private var _apiErrorResult = MutableLiveData<String>()
    var apiErrorResult: LiveData<String> = _apiErrorResult

    private var _networkErrorResult = MutableLiveData<String>()
    var networkErrorResult: LiveData<String> = _networkErrorResult

    private var _unknownErrorResult = MutableLiveData<String>()
    var unknownErrorResult: LiveData<String> = _unknownErrorResult

    val TAG = "calltest"
    fun searchMovie(title: String){
          viewModelScope.launch(mainDispatcher) {
              val response = tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, title, false)
              when (response) {
                  is NetworkResponse.Success -> {
                      Log.d(TAG, "Success ${response.body.results[0].title}")
                      _searchResult.value = response.body.results
                      _errorVisibility.value = true
                  }
                  is NetworkResponse.ApiError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.ApiError ->")
                      Log.d("svmlog", "code = ${response.body.cd}")
                      Log.d("svmlog", "msg = ${response.body.message}")
                      _apiErrorResult.value = context.resources.getString(R.string.api_error_msg)
                      _errorVisibility.value = true
                  }
                  is NetworkResponse.NetworkError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.NetworkError ->")
                      Log.d("svmlog", "cause = ${response.error.cause}")
                      Log.d("svmlog", "localizedMessage = ${response.error.localizedMessage}")
                      _networkErrorResult.value = context.resources.getString(R.string.network_error_msg)
                      _errorVisibility.value = true
                  }
                  is NetworkResponse.UnknownError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.UnknownError ->")
                      Log.d("svmlog", "cause = ${response.error?.cause}")
                      Log.d("svmlog", "localizedMessage = ${response.error?.localizedMessage}")
                      _unknownErrorResult.value = context.resources.getString(R.string.unknown_error_msg)
                      _errorVisibility.value = true
                  }
              }
          }
    }
}