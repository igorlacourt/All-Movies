package com.lacourt.myapplication.network

class Resource<T> private constructor(val status: Resource.Status, val data: T?) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }
        fun <T> error(): Resource<T> {
            return Resource(Status.ERROR, null)
        }
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data)
        }
    }
}