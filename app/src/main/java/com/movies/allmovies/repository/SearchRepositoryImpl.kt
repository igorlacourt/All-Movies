package com.movies.allmovies.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.movies.allmovies.AppConstants
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Apifactory
import com.movies.allmovies.ui.search.SearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(val context: Context): SearchRepository {
    var searchResult: MutableLiveData<ArrayList<MovieDTO>>? = MutableLiveData()

    override fun searchMovie(title:String, searchResultCallback: (result: SearchResult) -> Unit) {
        Apifactory.tmdbApi.searchMovie(AppConstants.LANGUAGE, title, false).enqueue(object : Callback<MovieResponseDTO> {
            override fun onFailure(call: Call<MovieResponseDTO>, t: Throwable) {
                searchResultCallback(SearchResult.ServerError)
            }
            override fun onResponse(call: Call<MovieResponseDTO>, responseDTO: Response<MovieResponseDTO>) {
                if(responseDTO.isSuccessful) {
                    Log.d("searchlog", "onSuccessful, result = ${responseDTO.body()}")
                    val list = responseDTO.body()?.results
                    list?.let {
                        searchResultCallback(SearchResult.Success(list))
                    }
                } else {
                    searchResultCallback(SearchResult.ApiError(responseDTO.code()))
                }
            }
        })
    }

}

sealed class SearchResult {
    class Success(val movies: ArrayList<MovieDTO>) : SearchResult()
    class ApiError(val statusCode: Int) : SearchResult()
    object ServerError : SearchResult()
}