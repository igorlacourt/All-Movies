package com.lacourt.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainmodel.MyListItem

class MyListViewModel(application: Application) : AndroidViewModel(application) {
    val repository = MyListRepository(application)
    val myList: LiveData<List<MyListItem>> = repository.myList
}

class MyListRepository(application: Application) {
    private val myListDao =
        AppDatabase.getDatabase(application)!!.MyListDao()
    val myList = MutableLiveData<List<MyListItem>>().apply {
        value = myListDao.all()
    }
}