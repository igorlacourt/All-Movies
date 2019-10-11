package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lacourt.myapplication.domainMappers.Mapper
import com.lacourt.myapplication.model.Details
import com.lacourt.myapplication.model.domainmodel.DomainModel
import com.lacourt.myapplication.network.Apifactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(application: Application) : AndroidViewModel(application), Mapper<Details, DomainModel> {
    internal var movie: LiveData<DomainModel>? = null
    private val repository: DetailsRepository = DetailsRepository(application, this)

    init {
        movie = repository.movie
    }

    fun fetchDetails(id: Int) {
        repository.fetchDetails(id)
    }

    override fun map(input: Details): DomainModel {
        return detailsToDomain(input)
    }

    private fun detailsToDomain(input: Details): DomainModel {
        return with(input) {
            DomainModel(
                backdrop_path,
                genres,
                id,
                overview,
                poster_path,
                release_date,
                title,
                vote_average
            )
        }
    }
}

class DetailsRepository(val application: Application, val detailsDataMapper: Mapper<Details, DomainModel>) {
    var movie: MutableLiveData<DomainModel> = MutableLiveData()

    fun fetchDetails(id: Int) {
        Apifactory.tmdbApi.getDetails(id).enqueue(object : Callback<Details> {
            override fun onFailure(call: Call<Details>, t: Throwable) {
                Toast.makeText(application, "details request FAIL", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Details>, response: Response<Details>) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        movie.value = detailsDataMapper.map(it)
                    }
                }
                else {
                    Toast.makeText(application, "Response NOT successful", Toast.LENGTH_LONG).show()
                }
            }

        })
    }
}