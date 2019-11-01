package com.lacourt.myapplication.network

import androidx.lifecycle.MutableLiveData

class LiveResource<T> : MutableLiveData<Resource<T>>()