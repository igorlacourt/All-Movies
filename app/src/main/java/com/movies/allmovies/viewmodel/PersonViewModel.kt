package com.movies.allmovies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.network.Resource

class PersonViewModel(application: Application) : AndroidViewModel(application){
    var movie: MutableLiveData<Resource<PersonDetails>>? = null
    var starredMovies: MutableLiveData<Resource<List<DomainMovie>>> = MutableLiveData()


}