package com.lacourt.myapplication.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.toDomiainMovie
import com.lacourt.myapplication.domainmodel.DomainMovie

class MyListRepository(application: Application) {
    private val myListDao =
        AppDatabase.getDatabase(application)!!.MyListDao()
    val myList = MutableLiveData<List<DomainMovie>>().apply {
        value = myListDao.all().toDomiainMovie()
    }
}