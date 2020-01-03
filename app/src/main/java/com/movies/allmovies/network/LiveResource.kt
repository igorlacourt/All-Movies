package com.movies.allmovies.network

import androidx.lifecycle.MutableLiveData

class LiveResource<T> : MutableLiveData<Resource<T>>()