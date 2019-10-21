package com.lacourt.myapplication.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.database.DatabaseCallback
import com.lacourt.myapplication.database.MyListDao
import com.lacourt.myapplication.domainMappers.MapperFunctions.toDetails
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.test_retrofit_call_kotlin.BaseRepository
import com.lacourt.myapplication.network.NetworkCallback
import com.lacourt.myapplication.network.NetworkCall
import com.lacourt.myapplication.network.Resource

import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class DetailsRepository(application: Application) : BaseRepository(), NetworkCallback<Details> {
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()
    var movie: MutableLiveData<Resource<Details>> = MutableLiveData()
    var myListMovie: MutableLiveData<Int> = MutableLiveData()

    var isInserted: MutableLiveData<Boolean> = Transformations.map(myListMovie, ::isInMyList)

//    fun isInMyList(myListItem: MyListItem): LiveData<Boolean> {
//
//    }

    fun getDetails(id: Int) {
        Log.d("calltest", "getDetails called")
        NetworkCall<DetailsDTO, Details>().makeCall(
            Apifactory.tmdbApi.getDetails(id),
            this,
            ::toDetails
        )

    }

    fun insert(myListItem: MyListItem) {
        myListDao?.insert(myListItem)
    }

    fun delete(databaseCallback: DatabaseCallback, id: Int) {
        Completable.fromAction { myListDao?.deleteById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    isInserted.value = false
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }
            })
    }

    override fun networkCallResult(callback: Resource<Details>) {
        movie.value = callback
        callback.data?.let {
            myListMovie = myListDao?.getById(it.id)
        }
        Log.d(
            "calltest",
            "networkCallResult, movie.value = ${movie.value?.data}"
        )
    }
}