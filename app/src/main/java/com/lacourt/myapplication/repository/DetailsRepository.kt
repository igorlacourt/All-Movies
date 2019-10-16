package com.lacourt.myapplication.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.MapperFunctions
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.test_retrofit_call_kotlin.BaseRepository
import com.lacourt.myapplication.network.NetworkCallback
import com.lacourt.myapplication.network.NetworkCall
import com.lacourt.myapplication.network.Resource

class DetailsRepository(val application: Application) : BaseRepository(),
    NetworkCallback<DetailsDTO> {
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()
    var movie: MutableLiveData<Resource<DetailsDTO>> = MutableLiveData()

    fun getDetails(id: Int) {
        Log.d("calltest", "getDetails called")
        NetworkCall<DetailsDTO>()
            .makeCall(Apifactory.tmdbApi.getDetails(id), this)

    }

    override fun networkCallResult(callback: Resource<DetailsDTO>) {
//        callback.data = MapperFunctions.toDetails(callback.data?)
        //TODO map the result to return a Details to the view
        movie.value = callback
        Log.d(
            "calltest",
            "networkCallResult, movie.value = ${movie.value?.data}"
        )
    }

    fun insert(myListItem: MyListItem) {
        myListDao?.insert(myListItem)
    }
}