package com.lacourt.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.model.dto.Movie
import com.lacourt.myapplication.model.dto.MovieResponse
import com.lacourt.myapplication.network.Apifactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository{
    var searchResult: MutableLiveData<ArrayList<Movie>>? = MutableLiveData()

    fun searchMovie(title:String) {
        Apifactory.tmdbApi.searchMovie(AppConstants.LANGUAGE, title, false).enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if(response.isSuccessful)
                    searchResult?.value = response.body()?.results
            }
        })
    }


}