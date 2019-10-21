package com.lacourt.myapplication.network

class Resource<R> private constructor(val status: Status, val data: R?){//}, val apiError:ApiError?) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }
    companion object {
        fun <R> success(data: R?): Resource<R> {
            return Resource(
                Status.SUCCESS,
                data
            )//, null)
        }
        fun <R> error(error: Error): Resource<R> {
            return Resource(
                Status.ERROR,
                null
            )
        }
        fun <R> loading(data: R?): Resource<R> {
            return Resource(
                Status.LOADING,
                data
            )//, null)
        }

//        fun <R> error(apiError: ApiError?): Resource<R> {
//            return Resource(Status.ERROR, null, apiError)
//        }
    }
}