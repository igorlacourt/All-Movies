package com.lacourt.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.domainMappers.Mapper
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.MovieResponse
import com.lacourt.myapplication.model.domainmodel.DataMovie
import com.lacourt.myapplication.model.domainmodel.MovieDomainModel
import com.lacourt.myapplication.network.Apifactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepositoryImpl(
    private val movieDataMapper: Mapper<DataMovie, MovieDomainModel>,
    private val productPreferences: MoviePreferences
)  {
    var searchResult: MutableLiveData<ArrayList<Movie>>? = MutableLiveData()

    fun searchMovie(title:String) {
        Apifactory.tmdbApi.searchMovie(AppConstants.LANGUAGE, title, false).enqueue(object :
            Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if(response.isSuccessful)
                    mapProducts(response.body()?.results as List<Movie>)
            }
        })
    }

    private fun mapProducts(networkMoviesList: List<Movie>): List<MovieDomainModel> {
        return networkMoviesList.map {
            movieDataMapper.map(DataMovie(it, productPreferences.isFavourite(it.id)))
        }
    }
}
// A DataSource for the SharedPreferences
interface MoviePreferences {
    fun isFavourite(id: Int?): Boolean
}















