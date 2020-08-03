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

class SearchViewModel @Inject constructor(private val tmdbApi: TmdbApi, @MainDispatcher val mainDispatcher: CoroutineDispatcher) : ViewModel() {

    private var _searchResult = MutableLiveData<List<MovieDTO>>()
    var searchResult: LiveData<List<MovieDTO>> = _searchResult

    private var _searchProgressBarVisibility = MutableLiveData<Boolean>(false)
    var searchProgressBarVisibility: LiveData<Boolean> = _searchProgressBarVisibility

    private var _errorScreenVisibility = MutableLiveData<Boolean>(false)
    var errorScreenVisibility: LiveData<Boolean> = _errorScreenVisibility

    private var _noResultsVisibility = MutableLiveData<Boolean>(true)
    var noResultsVisibility: LiveData<Boolean> = _noResultsVisibility

    private var _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    val TAG = "calltest"
    fun searchMovie(title: String){
        showSearchProgressBar(true)
        showError(false)
        viewModelScope.launch(mainDispatcher) {
              val response = tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, title, false)
             showSearchProgressBar(false)
              when (response) {
                  is NetworkResponse.Success -> {
                      Log.d(TAG, "Success")
                      _searchResult.value = response.body.results
                      showResults(response.body.results)
                  }
                  is NetworkResponse.ApiError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.ApiError ->")
                      Log.d("svmlog", "code = ${response.body.cd}")
                      Log.d("svmlog", "msg = ${response.body.message}")
                      _errorMessage.value = AppConstants.API_ERROR_MESSAGE
                      showErrorScreen()
                  }
                  is NetworkResponse.NetworkError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.NetworkError ->")
                      Log.d("svmlog", "cause = ${response.error.cause}")
                      Log.d("svmlog", "localizedMessage = ${response.error.localizedMessage}")
                      _errorMessage.value = AppConstants.NETWORK_ERROR_MESSAGE
                      showErrorScreen()
                  }

                  is NetworkResponse.UnknownError -> {
                      Log.d("svmlog", "_____________________________")
                      Log.d("svmlog", "NetworkResponse.UnknownError ->")
                      Log.d("svmlog", "cause = ${response.error?.cause}")
                      Log.d("svmlog", "localizedMessage = ${response.error?.localizedMessage}")
                      _errorMessage.value = AppConstants.UNKNOWN_ERROR_MESSAGE
                      showErrorScreen()
                  }
              }
          }
    }

    private fun showResults(results: List<MovieDTO>) {
        if (results.isNullOrEmpty()){
            showNoResultsMessage(true)
        } else {
            showNoResultsMessage(false)
        }
    }

    private fun showErrorScreen() {
        showError(true)
        showNoResultsMessage(false)
    }

    private fun showError(shouldShow: Boolean) {
        _errorScreenVisibility.value = shouldShow
    }

    private fun showSearchProgressBar(shouldShow: Boolean) {
        _searchProgressBarVisibility.value = shouldShow
    }

    private fun showNoResultsMessage(shouldShow: Boolean) {
        _noResultsVisibility.value = shouldShow
    }

}