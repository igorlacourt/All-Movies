package com.lacourt.myapplication.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.MapperFunctions.toDetails
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.network.NetworkCallback
import com.lacourt.myapplication.network.NetworkCall
import com.lacourt.myapplication.network.Resource

class DetailsRepository(val application: Application) : NetworkCallback<Details> {
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()
    var movie: MutableLiveData<Resource<Details>> = MutableLiveData()

    fun getDetails(id: Int) {
        Log.d("calltest", "getDetails called")
        NetworkCall<DetailsDTO, Details>().makeCall(Apifactory.tmdbApi.getDetails(id), this, ::toDetails)

    }

    fun insert(myListItem: MyListItem) {
        myListDao?.insert(myListItem)
    }

    override fun networkCallResult(callback: Resource<Details>) {
        movie.value = callback
        Log.d(
            "calltest",
            "networkCallResult, movie.value = ${movie.value?.data}"
        )
    }
}