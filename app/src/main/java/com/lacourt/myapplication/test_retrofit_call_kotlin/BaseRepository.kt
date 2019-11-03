package com.lacourt.myapplication.test_retrofit_call_kotlin

import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.network.TmdbApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(var apiServices: TmdbApi):BaseRepository(){

    fun getDetails(id: Int, callBack: Callback<DetailsDTO>) {
        apiServices.getDetails(575452, AppConstants.VIDEOS).makeCall {
            onResponseSuccess = {
//             it.body().
            }
            onResponseFailure = {

            }
        }
    }
}

open class BaseRepository {

    fun <T> Call<T>.makeCall(callback: CallBackKt<T>.() -> Unit) {
        val callBackKt = CallBackKt<T>()
        callback.invoke(callBackKt)
        this.enqueue(callBackKt)
    }

    class CallBackKt<T>: Callback<T> {
        var onResponseSuccess: ((Response<T>) -> Unit)? = null
        var onResponseFailure: ((t: Throwable?) -> Unit)? = null

        override fun onFailure(call: Call<T>, t: Throwable) {
            onResponseFailure?.invoke(t)
            //TODO deal with failure cases
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            onResponseSuccess?.invoke(response)
            //TODO insert if isSuccessful check and deal with error cases
        }
    }

}
