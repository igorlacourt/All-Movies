package com.movies.allmovies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.repository.MyListRepository

class MyListViewModel(application: Application) : AndroidViewModel(application) {
    val repository = MyListRepository(application)
    val myList: LiveData<List<DomainMovie>> = repository.myList
    fun getList(){
        repository.getList()
    }
}

