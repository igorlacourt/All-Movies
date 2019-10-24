package com.lacourt.myapplication.repository

import android.annotation.SuppressLint
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
    override fun trendingAllCallback(callback: Resource<Details>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun upcomingMoviesCallback(callback: Resource<Details>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun popularMoviesCallback(callback: Resource<Details>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun popularSeriesCallback(callback: Resource<Details>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun topRatedMoviesCallback(callback: Resource<Details>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun topRatedSeriesCallback(callback: Resource<Details>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()
    var movie: MutableLiveData<Resource<Details>> = MutableLiveData()
    var isInDatabase: MutableLiveData<Boolean> = MutableLiveData()

    fun getDetails(id: Int) {
        Log.d("calltest", "getDetails called")
        NetworkCall<DetailsDTO, Details>().makeCall(
            Apifactory.tmdbApi.getDetails(id),
            this,
            ::toDetails
        )

    }

    fun insert(myListItem: MyListItem) {
        Log.d("log_is_inserted", "DetailsRepository, insert() called")
        myListDao?.insert(myListItem)
    }

    fun delete(id: Int) {
        Log.d("log_is_inserted", "DetailsRepository, delete() called")
        Completable.fromAction { myListDao?.deleteById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onComplete() called")
                    isInDatabase.value = false
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onError() called")
                }
            })
    }

    @SuppressLint("CheckResult")
    override fun networkCallResult(callback: Resource<Details>) {
        Log.d("log_is_inserted", "DetailsRepository, networkCallResult() called, movie = ${callback.data?.title}")
        movie.value = callback
        val id: Int? = callback.data?.id
        Log.d("log_is_inserted", "DetailsRepository, networkCallResult() called, id = $id")
        if (id != null) {
            isInDatabase.value = false
            myListDao?.getById(id)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnNext{
                    isInDatabase.value = true
                    Log.d("log_is_inserted", "DetailsRepository, getById(), doOnNext called, isInDatabase.value = ${isInDatabase.value}")
                }
                ?.subscribe()
        }
//            ?.subscribe {
                //databaseCallback.onUsersLoaded(users);
//            }

        Log.d(
            "calltest",
            "networkCallResult, movie.value = ${movie.value?.data}"
        )
    }
}