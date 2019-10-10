package com.lacourt.myapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.MovieResponse
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.network.TmdbApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository() {
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