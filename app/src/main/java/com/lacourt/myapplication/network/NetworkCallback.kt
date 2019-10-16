package com.lacourt.myapplication.network

interface NetworkCallback<T>{
    fun networkCallResult(callback: Resource<T>)
}