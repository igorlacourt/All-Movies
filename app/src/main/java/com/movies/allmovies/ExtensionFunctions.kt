package com.movies.allmovies

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.movies.allmovies.database.MyListDao
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.MyListItem
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun Details.openYoutube(context: Context?) {
    if (context != null) {
        if (!this.videos.isNullOrEmpty() && !this.videos[0].key.isNullOrEmpty()) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=${this.videos[0].key}")
            )
            context.startActivity(webIntent)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.no_video_to_show),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


fun MyListDao?.deleteById(context: Context, id: Int?, isInDatabase: MutableLiveData<Boolean>) {
    if (id != null && this != null) {

        val disposable = CompositeDisposable()

        Completable.fromAction { this.deleteById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( object : CompletableObserver {
                override fun onComplete() {
                    isInDatabase.postValue(false)
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onComplete() called")
                    Log.d("mylistclick", "ExtensionFunctions, deleteById(), onComplete() called")
                    disposable.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onError() called")
                    Log.d("mylistclick", "ExtensionFunctions, deleteById(), onError() called")
                    disposable.dispose()
                }
            })

    } else {
        Toast.makeText(context, context.getString(R.string.database_item_not_removed), Toast.LENGTH_SHORT).show()
        Log.d("mylistclick", "ExtensionFunctions, deleteById(),id or myListDao is null")
    }
}

fun MyListDao?.insertItem(context: Context, item: MyListItem?, isInDatabase: MutableLiveData<Boolean>) {
    if (item != null && this != null) {
        val disposable = CompositeDisposable()

        Completable.fromAction { this.insert(item) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( object : CompletableObserver {
                override fun onComplete() {
                    isInDatabase.postValue(true)
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onComplete() called")
                    Log.d("mylistclick", "ExtensionFunctions, deleteById(), onComplete() called")
                    disposable.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onError() called")
                    Log.d("mylistclick", "ExtensionFunctions, deleteById(), onError() called")
                    Toast.makeText(context, context.getString(R.string.database_item_not_inserted), Toast.LENGTH_SHORT).show()
                    disposable.dispose()
                }
            })

    } else {
        Toast.makeText(context, context.getString(R.string.database_item_not_inserted), Toast.LENGTH_SHORT).show()
        Log.d("mylistclick", "ExtensionFunctions, deleteById(),id or myListDao is null")
    }
}

fun MyListDao?.isInDatabase(id: Int?, isInDatabase: MutableLiveData<Boolean>) {
    isInDatabase.postValue(false)

    val disposable = CompositeDisposable()

    if (id != null && this != null) {
//        this.getById(id)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            ?.doOnNext {
//                isInDatabase.postValue(true)
//                Log.d(
//                    "log_is_inserted",
//                    "ExtensionFunction.IsInDatabase(), getById(), doOnNext called, isInDatabase.value = $isInDatabase"
//                )
//            }
//            ?.doOnSubscribe {  }
//            ?.doOnComplete {  }
//            ?.subscribe()
    }
}