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
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val context: Context, private val tmdbApi: TmdbApi, @MainDispatcher val mainDispatcher: CoroutineDispatcher) : ViewModel() {

    private var _searchResult = MutableLiveData<List<MovieDTO>>()
    var searchResult: LiveData<List<MovieDTO>> = _searchResult

    private var _searchProgressBarVisibility = MutableLiveData<Boolean>(false)
    var searchProgressBarVisibility: LiveData<Boolean> = _searchProgressBarVisibility

    private var _noResultsVisibility = MutableLiveData<Boolean>(true)
    var noResultsVisibility: LiveData<Boolean> = _noResultsVisibility

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    val TAG = "calltest"
    fun searchMovie(title: String){
        searchProgressBarVisible(true)
        viewModelScope.launch(mainDispatcher) {
              val response = tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, title, false)
              when (response) {
                  is NetworkResponse.Success -> {
                      Log.d(TAG, "Success")
                      _searchResult.value = response.body.results
                      if (response.body.results.isNullOrEmpty()){
                          noResultsVisible(true)
                      }
                      searchProgressBarVisible(false)
                      noResultsVisible(false)
                  }
                  is NetworkResponse.ApiError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.ApiError ->")
                      Log.d("svmlog", "code = ${response.body.cd}")
                      Log.d("svmlog", "msg = ${response.body.message}")
                      _errorMessage.value = context.resources.getString(R.string.api_error_msg)
                      searchProgressBarVisible(false)
                      noResultsVisible(false)
                  }
                  is NetworkResponse.NetworkError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.NetworkError ->")
                      Log.d("svmlog", "cause = ${response.error.cause}")
                      Log.d("svmlog", "localizedMessage = ${response.error.localizedMessage}")
                      _errorMessage.value = context.resources.getString(R.string.network_error_msg)
                      searchProgressBarVisible(false)
                      noResultsVisible(false)
                  }

                  is NetworkResponse.UnknownError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.UnknownError ->")
                      Log.d("svmlog", "cause = ${response.error?.cause}")
                      Log.d("svmlog", "localizedMessage = ${response.error?.localizedMessage}")
                      _errorMessage.value = context.resources.getString(R.string.unknown_error_msg)
                      searchProgressBarVisible(false)
                      noResultsVisible(false)
                  }
              }
          }
    }

    private fun searchProgressBarVisible(isVisible: Boolean) {
        _searchProgressBarVisibility.value = isVisible
    }

    private fun noResultsVisible(isVisible: Boolean) {
        _noResultsVisibility.value = isVisible
    }

}