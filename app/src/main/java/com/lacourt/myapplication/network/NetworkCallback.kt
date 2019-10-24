package com.lacourt.myapplication.network

interface NetworkCallback<T>{
    fun networkCallResult(callback: Resource<T>)
    fun trendingAllCallback(callback: Resource<T>)
    fun upcomingMoviesCallback(callback: Resource<T>)
    fun popularMoviesCallback(callback: Resource<T>)
    fun popularSeriesCallback(callback: Resource<T>)
    fun topRatedMoviesCallback(callback: Resource<T>)
    fun topRatedSeriesCallback(callback: Resource<T>)
}