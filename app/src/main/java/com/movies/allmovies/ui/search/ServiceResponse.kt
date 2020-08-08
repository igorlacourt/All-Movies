package com.movies.allmovies.ui.search

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.Resource
//import com.squareup.moshi.JsonEncodingException
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface ApiResponse<T> {
    suspend fun result(): Resource<T>
    fun success(data: T): Resource<T>
    fun error(code: Int, errorBody: ResponseBody?): Resource<T>
    fun failure(exception: Exception): Resource<T>
}

class ServiceResponse<T>(private val context: Context?, private val request: suspend () -> Response<T>) : ApiResponse<T> {
    override suspend fun result(): Resource<T> {
        return try {
            val response = request()
            val data = response.body()
            if(response.isSuccessful && data != null) {
                success(data)
            } else {
                error(response.code(), response.errorBody())
            }
        } catch (exception: Exception) {
            failure(exception)
        }
    }

    override fun success(data: T): Resource<T> {
        return Resource.success(data)
    }

    override fun error(code: Int, errorBody: ResponseBody?): Resource<T> {
        val error: Error = if (errorBody != null) {
            when (code) {
                400 -> {
                    Log.d("errorBoolean", "HomeRepository, is SocketTimeoutException")
                    Error(400, "SocketTimeoutException")
                }
                404 -> {
                    Log.d("errorBoolean", "HomeRepository, is UnknownHostException")
                    Error(99, "UnknownHostException")
                }
                401 -> {
                    Log.d("errorBoolean", "HomeRepository, is HttpException")
                    Error(code, errorBody.string())
                }
                else -> {
                    Log.d("errorBoolean", "HomeRepository, is Another Error")
                    Error(0, errorBody.string())
                }
            }
        } else {
            Error(code, "Unknown error")
        }
        return Resource.error(error)
    }
// TODO Test failure() and error() cases
    override fun failure(exception: Exception): Resource<T> {
        val error = when (exception) {
            is SocketTimeoutException -> {
                Log.d("errorBoolean", "RetrofitResponse, is SocketTimeoutException")
                Error(408, "SocketTimeoutException")
            }
            is UnknownHostException -> {
                Log.d("errorBoolean", "RetrofitResponse, is UnknownHostException")
                Error(99, "UnknownHostException")
            }
            is HttpException -> {
                Log.d("errorBoolean", "RetrofitResponse, is HttpException")
                Error(exception.code(), exception.message())
            }
            is IOException -> {
                Log.d("errorBoolean", "RetrofitResponse, is IOException")
                Error(1, "IOException")
            }
//            is JsonEncodingException -> { // needs moshi dependency
//                Log.d("errorBoolean", "RetrofitResponse, is JsonEncodingException")
//                Error(2, "JsonEncondingException")
//            }
            else -> {
                Log.d("errorBoolean", "RetrofitResponse, is Another Error")
                Error(0, exception.message)
            }
        }
        return Resource.error(error)
    }

    private fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            // don't check internet error for lower apis
            return true
        }
    }
}