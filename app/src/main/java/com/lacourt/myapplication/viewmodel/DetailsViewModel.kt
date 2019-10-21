package com.lacourt.myapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.lacourt.myapplication.R
import com.lacourt.myapplication.database.DatabaseCallback
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.repository.DetailsRepository
import com.lacourt.myapplication.network.Resource

class DetailsViewModel(application: Application) : AndroidViewModel(application), DatabaseCallback{

    val app : Application = application
    private val repository: DetailsRepository = DetailsRepository(application)
    internal var movie: MutableLiveData<Resource<Details>>? = null
    lateinit var isInserted: LiveData<Boolean>

    init {
        isInserted = repository.isInserted
    }

    fun insert(myListItem: MyListItem) {
        repository.insert(myListItem)
    }

    fun getDetails(id: Int) {
        Log.d("calltest", "fetchDetails called")
        movie = repository.movie
        repository.getDetails(id)
    }

    fun delete(id: Int){
        isInserted = repository.isInserted
        repository.delete(id)
    }

    override fun onItemDeleted() {
        Toast.makeText(app, "").show()
    }



}