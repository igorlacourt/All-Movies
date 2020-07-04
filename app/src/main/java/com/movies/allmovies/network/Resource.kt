package com.movies.allmovies.network

class Resource<R> private constructor(val status: Status, val data: R? = null, val error: Error? = null){//}, val apiError:ApiError?) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }
    companion object {
        fun <R> success(data: R?): Resource<R> {
            return Resource(
                Status.SUCCESS,
                data
            )
        }
        fun <R> error(error: Error?): Resource<R> {
            return Resource(
                Status.ERROR,
                error = error
            )
        }
        fun <R> loading(data: R?): Resource<R> {
            return Resource(
                Status.LOADING
            )
        }

//        fun <R> error(apiError: ApiError?): Resource<R> {
//            return Resource(Status.ERROR, null, apiError)
//        }
    }
}