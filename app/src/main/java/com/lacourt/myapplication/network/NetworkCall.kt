package com.lacourt.myapplication.network

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class NetworkCall<T, R> {
    fun makeCall(call: Call<T>, networkCallback: NetworkCallback<R>, map: (T) -> R) {
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {

            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                networkCallback.networkCallResult(
                    Resource.success(
                        response.body()?.let { map(it) })
                )
            }

        })
    }

    class CallBackKt<T> : Callback<T> {
        var result: MutableLiveData<Resource<T>> = MutableLiveData()

        override fun onFailure(call: Call<T>, t: Throwable) {
            result.value = Resource.error()
            t.printStackTrace()
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful)

            else {

            }
        }
    }

    fun cancel() {
//        if (::call.isInitialized) {
//            call.cancel()
//        }
    }
}