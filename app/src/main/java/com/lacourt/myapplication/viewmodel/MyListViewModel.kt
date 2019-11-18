package com.lacourt.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainmodel.DomainMovie
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.repository.MyListRepository

class MyListViewModel(application: Application) : AndroidViewModel(application) {
    val repository = MyListRepository(application)
    val myList: LiveData<List<DomainMovie>> = repository.myList
    fun getList(){
        repository.getList()
    }
}

