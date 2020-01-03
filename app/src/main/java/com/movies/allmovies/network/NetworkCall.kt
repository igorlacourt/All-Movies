package com.movies.allmovies.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class NetworkCall<T, R> {
    fun makeCall(call: Call<T>, callback: NetworkCallback<R>, map: (T) -> R) {
        Log.d("calltest", "makeCall called")
        call.enqueue(object : Callback<T> {

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.networkCallResult(Resource.error(
                    Error(0, t.message)
                ))
                t.printStackTrace()
                Log.d(
                    "calltest",
                    "onFailure, throwable massage = ${t.localizedMessage}"
                )
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.networkCallResult(
                        Resource.success(
                            response.body()?.let { map(it) }
                        )
                    )
                    Log.d(
                        "calltest",
                        "onResponse successful, ${response.body()}"
                    )
                } else {
                    callback.networkCallResult(Resource.error(
                        Error(response.code(), response.message())
                    ))
                    Log.d(
                        "calltest",
                        "onResponse NOT successful, response.code() = ${response.code()}"
                    )
                }
            }

        })
    }

//    fun cancel() {
//        if (::call.isInitialized) {
//            call.cancel()
//        }
//    }
}