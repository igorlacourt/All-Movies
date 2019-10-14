package com.lacourt.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.dto.MovieResponseDTO
import com.lacourt.myapplication.network.Apifactory
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
                    searchResult?.value = responseDTO.body()?.results
            }
        })
    }


}