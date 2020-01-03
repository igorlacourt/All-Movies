package com.movies.allmovies.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.movies.allmovies.AppConstants
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Apifactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository{
    var searchResult: MutableLiveData<ArrayList<MovieDTO>>? = MutableLiveData()

    fun searchMovie(title:String) {
        Apifactory.tmdbApi.searchMovie(AppConstants.LANGUAGE, title, false).enqueue(object : Callback<MovieResponseDTO> {
            override fun onFailure(call: Call<MovieResponseDTO>, t: Throwable) {

            }
            override fun onResponse(call: Call<MovieResponseDTO>, responseDTO: Response<MovieResponseDTO>) {
                if(responseDTO.isSuccessful)
                    Log.d("searchlog", "onSuccessful, result = ${responseDTO.body()}")
                    searchResult?.value = responseDTO.body()?.results
            }
        })
    }


}