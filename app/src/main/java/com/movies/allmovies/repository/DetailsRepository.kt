package com.movies.allmovies.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.movies.allmovies.AppConstants
import com.movies.allmovies.database.AppDatabase
import com.movies.allmovies.deleteById
import com.movies.allmovies.domainMappers.MapperFunctions.toDetails
import com.movies.allmovies.domainMappers.toDomainMovie
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.CastDTO
import com.movies.allmovies.dto.DetailsDTO
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.insertItem
import com.movies.allmovies.isInDatabase
import com.movies.allmovies.network.Apifactory
import com.movies.allmovies.network.NetworkCall
import com.movies.allmovies.network.NetworkCallback
import com.movies.allmovies.network.Resource
import com.movies.allmovies.test_retrofit_call_kotlin.BaseRepository
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsRepository(application: Application) : BaseRepository(), NetworkCallback<Details> {
    private val myListDao =
        AppDatabase.getDatabase(application)?.MyListDao()
    var movie: MutableLiveData<Resource<Details>> = MutableLiveData()
    var recommendedMovies: MutableLiveData<Resource<List<DomainMovie>>> = MutableLiveData()
    var isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val context: Context = application

    fun getDetails(id: Int) {
        Log.d("calltest", "getDetails called")
        NetworkCall<DetailsDTO, Details>().makeCall(
            Apifactory.tmdbApi.getDetails(id, AppConstants.VIDEOS_AND_CASTS),
            this,
            ::toDetails
        )
    }

    fun getRecommendedMovies(id: Int) {
        //  This checked was inserted for changing the id of frozen2 to the id of Frozen1 because the similar results were
        //displaying unappropriated results for children
        var checkedId: Int
        checkedId = id
        if(checkedId == 330457) {
            checkedId = 109445
        }
        val disposable = CompositeDisposable()
        Apifactory.tmdbApi.getRecommendations(checkedId, AppConstants.LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<MovieResponseDTO> {
                override fun onSuccess(t: MovieResponseDTO) {
                    val list = ArrayList<MovieDTO>()
                    list.addAll(t.results)
                    if(list.size  < 3){
                        getSimilar(checkedId)
                    } else {
                        if (list.size == 20) {
                            val last = t.results.size - 1
                            val beforeLast = t.results.size - 2
                            list.removeAt(last)
                            list.removeAt(beforeLast)
                        }
                        recommendedMovies.value =
                            Resource.success(t.toDomainMovie() as List)
                    }
                    disposable.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onError(e: Throwable) {
                    disposable.dispose()
                }
            })
    }

    fun getSimilar(id: Int) {
        val disposable = CompositeDisposable()
        Apifactory.tmdbApi.getSimilar(id, AppConstants.LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<MovieResponseDTO> {
                override fun onSuccess(t: MovieResponseDTO) {
                    val list = ArrayList<MovieDTO>()
                    list.addAll(t.results)
                    if (t.results.size == 20) {
                        val last = t.results.size - 1
                        val beforeLast = t.results.size - 2
                        list.removeAt(last)
                        list.removeAt(beforeLast)
                    }
                    recommendedMovies.value =
                        Resource.success(t.toDomainMovie() as List)

                    disposable.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onError(e: Throwable) {
                    disposable.dispose()
                }
            })
    }

    fun isInDatabase(id: Int?) {
        id?.let { myListDao.isInDatabase(id, isInDatabase) }
    }

    fun insert(myListItem: MyListItem) {
        Log.d("log_is_inserted", "DetailsRepository, insert() called")
        myListDao?.insertItem(context, myListItem, isInDatabase)
    }

    fun delete(id: Int) {
        Log.d("log_is_inserted", "DetailsRepository, delete() called")
        myListDao.deleteById(context, id, isInDatabase)
    }

    @SuppressLint("CheckResult")
    override fun networkCallResult(callback: Resource<Details>) {
        Log.d(
            "log_is_inserted",
            "DetailsRepository, networkCallResult() called, movie = ${callback.data?.title}"
        )

        myListDao.isInDatabase(callback.data?.id, isInDatabase)

        movie.value = callback

//        val id: Int? = callback.data?.id
//        Log.d("log_is_inserted", "DetailsRepository, networkCallResult() called, id = $id")
//        if (id != null) {
//            isInDatabase.value = false
//            myListDao?.getById(id)
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.doOnNext {
//                    isInDatabase.value = true
//                    Log.d(
//                        "log_is_inserted",
//                        "DetailsRepository, getById(), doOnNext called, isInDatabase.value = ${isInDatabase.value}"
//                    )
//                }
//                ?.subscribe()
//        }

        Log.d(
            "calltest",
            "networkCallResult, movie.value = ${movie.value?.data}"
        )
    }
}






