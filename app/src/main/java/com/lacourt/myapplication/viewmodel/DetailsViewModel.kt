package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.material.snackbar.Snackbar
import com.lacourt.myapplication.R
import com.lacourt.myapplication.database.DatabaseCallback
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.repository.DetailsRepository
import com.lacourt.myapplication.network.Resource

class DetailsViewModel(application: Application) : AndroidViewModel(application){
    val app : Application = application
    private val repository: DetailsRepository = DetailsRepository(application)
    internal var movie: MutableLiveData<Resource<Details>>? = null
    var isInDatabase: LiveData<Boolean> = repository.isInDatabase

    fun isInMyList(id: Int?): Boolean {
        if (id == null)
            return false
        return true
    }

    fun insert(myListItem: MyListItem) {
        Log.d("log_is_inserted", "DetailsViewModel, insert() called")
        repository.insert(myListItem)
    }

    fun getDetails(id: Int) {
        Log.d("calltest", "fetchDetails called")
        movie = repository.movie
        repository.getDetails(id)
    }

    fun delete(id: Int){
        Log.d("log_is_inserted", "DetailsViewModel, delete() called")

        repository.delete(id)
    }

}