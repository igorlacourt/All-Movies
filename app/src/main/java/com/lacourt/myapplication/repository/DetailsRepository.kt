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
import com.lacourt.myapplication.test_retrofit_call_kotlin.BaseRepository
import com.lacourt.myapplication.test_retrofit_call_kotlin.other_example.NetworkCall
import com.lacourt.myapplication.test_retrofit_call_kotlin.other_example.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsRepository(val application: Application) : BaseRepository() {
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()
        var movie: MutableLiveData<Details> = MutableLiveData()
//    var movie: MutableLiveData<Resource<DetailsDTO>> = MutableLiveData()

    fun getDetails(id: Int) {
        Log.d("calltest", "getDetails called")

//        var networkCall = NetworkCall<DetailsDTO>()
//        networkCall.makeCall(Apifactory.tmdbApi.getDetails(id))
//        var details = networkCall.result.value?.data?.let { MapperFunctions.toDetails(it) }
//
//        if (details != null)
//            movie.value = details

        Apifactory.tmdbApi.getDetails(id).makeCall {
            onResponseSuccess = {
                if (it.isSuccessful) {
                    movie.value =
                        it.body()?.let { detailsDTO -> MapperFunctions.toDetails(detailsDTO) }
                } else {
                    Toast.makeText(application, "Request not successful code ${it.code()}", Toast.LENGTH_LONG).show()
                }
            }
            onResponseFailure = {
                Toast.makeText(application, "Request fail", Toast.LENGTH_LONG).show()
            }
        }
//        Log.d("calltest", "getDetails dto called, title ${movieDTO?.title}")
//        movie.value = movieDTO?.let { MapperFunctions.toDetails(movieDTO) }

//        Log.d("calltest", "getDetails called, title ${movie.value?.title}")
    }

//    fun fetchDetails(id: Int) {
//        Apifactory.tmdbApi.getDetails(id).enqueue(object : Callback<DetailsDTO> {
//            override fun onFailure(call: Call<DetailsDTO>, t: Throwable) {
//                Toast.makeText(application, "details request FAIL", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onResponse(call: Call<DetailsDTO>, response: Response<DetailsDTO>) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        movie?.value = MapperFunctions.toDetails(it)
//                    }
//                } else {
//                    Toast.makeText(application, "Response NOT successful", Toast.LENGTH_LONG).show()
//                }
//            }
//
//        })
//    }

    fun insert(myListItem: MyListItem) {
        myListDao?.insert(myListItem)
    }
}