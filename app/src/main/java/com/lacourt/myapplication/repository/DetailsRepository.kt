package com.lacourt.myapplication.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.MapperFunctions
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.test_retrofit_call_kotlin.other_example.NetworkCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsRepository(val application: Application) {
    var movie: MutableLiveData<Details> = MutableLiveData()
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()

    fun getDetails(id: Int) {
        val movieDTO =
            NetworkCall<DetailsDTO>().makeCall(Apifactory.tmdbApi.getDetails(575452)).value?.data
        Log.d("calltest", "getDetails dto called, title ${movieDTO?.title}")
        movie.value = movieDTO?.let { MapperFunctions.toDetails(it) }
        Log.d("calltest", "getDetails called, title ${movie.value?.title}")
    }

    fun fetchDetails(id: Int) {
        Apifactory.tmdbApi.getDetails(id).enqueue(object : Callback<DetailsDTO> {
            override fun onFailure(call: Call<DetailsDTO>, t: Throwable) {
                Toast.makeText(application, "details request FAIL", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DetailsDTO>, response: Response<DetailsDTO>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        movie.value = MapperFunctions.toDetails(it)
                    }
                } else {
                    Toast.makeText(application, "Response NOT successful", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun insert(myListItem: MyListItem) {
        myListDao?.insert(myListItem)
    }
}