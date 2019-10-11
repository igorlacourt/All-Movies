package com.lacourt.myapplication.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.Mapper
import com.lacourt.myapplication.model.dbmodel.MyListItem
import com.lacourt.myapplication.model.dto.Details
import com.lacourt.myapplication.model.domainmodel.DomainModel
import com.lacourt.myapplication.network.Apifactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsRepository(val application: Application, val detailsDataMapper: Mapper<Details, DomainModel>) {
    var movie: MutableLiveData<DomainModel> = MutableLiveData()
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()

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

    fun insert(myListItem: MyListItem){
        myListDao?.insert(myListItem)
    }
}