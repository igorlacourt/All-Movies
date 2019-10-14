package com.lacourt.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.domainMappers.not_used_interfaces.Mapper
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.dto.MovieResponseDTO
import com.lacourt.myapplication.domainmodel.DataMovie
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.network.Apifactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepositoryImpl(
    private val dataMapper: Mapper<DataMovie, Details>,
    private val productPreferences: MoviePreferences
)  {
    var searchResult: MutableLiveData<ArrayList<MovieDTO>>? = MutableLiveData()

    fun searchMovie(title:String) {
        Apifactory.tmdbApi.searchMovie(AppConstants.LANGUAGE, title, false).enqueue(object :
            Callback<MovieResponseDTO> {
            override fun onFailure(call: Call<MovieResponseDTO>, t: Throwable) {

            }
            override fun onResponse(call: Call<MovieResponseDTO>, responseDTO: Response<MovieResponseDTO>) {
                if(responseDTO.isSuccessful)
                    mapProducts(responseDTO.body()?.results as List<MovieDTO>)
            }
        })
    }

    private fun mapProducts(networkMoviesList: List<MovieDTO>): List<Details> {
        return networkMoviesList.map {
            dataMapper.map(
                DataMovie(
                    it,
                    productPreferences.isFavourite(it.id)
                )
            )
        }
    }
}
// A DataSource for the SharedPreferences
interface MoviePreferences {
    fun isFavourite(id: Int?): Boolean
}















