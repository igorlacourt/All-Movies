package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lacourt.myapplication.model.Details
import com.lacourt.myapplication.model.domainmodel.DomainModel
import com.lacourt.myapplication.network.Apifactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    var movie: LiveData<DomainModel>? = null
    private val repository: DetailsRepository = DetailsRepository(application)

    init {
        Log.d("testdetails", "viewmodel init called")
        movie = repository.movie
    }

    fun fetchDetails(id: Int) {
        repository.fetchDetails(id)
    }

}

class DetailsRepository(val application: Application) {
    var movie: MutableLiveData<DomainModel>? = null

    fun fetchDetails(id: Int) {
        Log.d("testdetails", "repository, fetchDetails called")
        Apifactory.tmdbApi.getDetails(id).enqueue(object : Callback<Details> {
            override fun onFailure(call: Call<Details>, t: Throwable) {
                Log.d("testdetails", "repository, details request FAIL")
                Toast.makeText(application, "details request FAIL", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Details>, response: Response<Details>) {
                Log.d("testdetails", "repository, onResponse")
                if(response.isSuccessful) {
                    Log.d("testdetails", "repository, response successful")
                    // TODO testar o map
                    response.body()?.let { mapResult(it) }
                }
                else {
                    Log.d("testdetails", "repository, response NOT successful")
                    Toast.makeText(application, "Response NOT successful", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun mapResult(input: Details): DomainModel {
        return map(input)
    }

    fun map(input: Details): DomainModel {
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