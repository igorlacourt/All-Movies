package com.lacourt.myapplication.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.dto.MovieResponseDTO
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.network.TmdbApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchRepository  {

    var searchResult: MutableLiveData<ArrayList<MovieDTO>>? = MutableLiveData()

    fun searchMovie(title: String) {
        Apifactory.tmdbApi.searchMovie(AppConstants.LANGUAGE, title, false)
            .enqueue(object : Callback<MovieResponseDTO> {
                override fun onFailure(call: Call<MovieResponseDTO>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<MovieResponseDTO>,
                    responseDTO: Response<MovieResponseDTO>
                ) {
                    if (responseDTO.isSuccessful)
                        Log.d("searchlog", "onSuccessful, result = ${responseDTO.body()}")
                    searchResult?.value = responseDTO.body()?.results
                }
            })

//        Apifactory.tmdbApi.searchMovie(AppConstants.LANGUAGE, title, false).enqueue(object : Callback<MovieResponseDTO> {
//            override fun onFailure(call: Call<MovieResponseDTO>, t: Throwable) {
//
//            }
//            override fun onResponse(call: Call<MovieResponseDTO>, responseDTO: Response<MovieResponseDTO>) {
//                if(responseDTO.isSuccessful)
//                    Log.d("searchlog", "onSuccessful, result = ${responseDTO.body()}")
//                searchResult?.value = responseDTO.body()?.results
//            }
//        })
    }


}