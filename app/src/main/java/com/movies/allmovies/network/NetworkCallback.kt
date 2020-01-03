package com.movies.allmovies.network

interface NetworkCallback<T>{
    fun networkCallResult(callback: Resource<T>)
}