package com.lacourt.myapplication.network

interface NetworkCallback<R>{
    fun networkCallResult(callback: Resource<R>)
}