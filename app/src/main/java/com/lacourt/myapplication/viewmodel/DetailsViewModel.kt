package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.repository.DetailsRepository
import com.lacourt.myapplication.network.Resource

class DetailsViewModel(application: Application) : AndroidViewModel(application){
    private val repository: DetailsRepository = DetailsRepository(application)
    internal var movie: MutableLiveData<Resource<DetailsDTO>>? = null

    fun insert(myListItem: MyListItem) {
        repository.insert(myListItem)
    }

    fun getDetails(id: Int) {
        Log.d("calltest", "fetchDetails called")
        movie = repository.movie
        repository.getDetails(id)
    }

}