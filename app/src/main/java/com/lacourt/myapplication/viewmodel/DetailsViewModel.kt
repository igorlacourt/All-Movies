package com.lacourt.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.repository.DetailsRepository

class DetailsViewModel(application: Application) : AndroidViewModel(application){
    internal var movie: LiveData<Details>? = null
    private val repository: DetailsRepository = DetailsRepository(application)

    init {
        movie = repository.movie
    }

    fun insert(myListItem: MyListItem) {
        repository.insert(myListItem)
    }

    fun fetchDetails(id: Int) {
        repository.getDetails(id)
//        repository.fetchDetails(id)
    }

}