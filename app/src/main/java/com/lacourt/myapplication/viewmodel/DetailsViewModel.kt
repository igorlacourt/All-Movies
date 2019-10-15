package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.repository.DetailsRepository
import com.lacourt.myapplication.test_retrofit_call_kotlin.other_example.Resource

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DetailsRepository = DetailsRepository(application)
    internal var movie: LiveData<Details> = repository.movie as LiveData<Details>
//    internal var movie: LiveData<Resource<DetailsDTO>> = repository.movie

    fun insert(myListItem: MyListItem) {
        repository.insert(myListItem)
    }

    fun fetchDetails(id: Int) {
        Log.d("calltest", "fetchDetails called")
        repository.getDetails(id)
//        repository.fetchDetails(id)
    }

}