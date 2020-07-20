package com.movies.allmovies.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.movies.allmovies.database.AppDatabase
import com.movies.allmovies.domainmappers.toDomainMovie
import com.movies.allmovies.domainmodel.DomainMovie

class MyListRepository(application: Application) {
    private val myListDao =
        AppDatabase.getDatabase(application)!!.MyListDao()
    val myList = MutableLiveData<List<DomainMovie>>()

    fun getList(){
        myList.value = myListDao.all().toDomainMovie()
    }
}



