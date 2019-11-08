package com.lacourt.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.lacourt.myapplication.database.MyListDao
import com.lacourt.myapplication.domainmodel.Details
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
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


fun MyListDao?.deleteByIdExt(context: Context, id: Int?, isInDatabase: MutableLiveData<Boolean>) {
    if (id != null && this != null) {
        Completable.fromAction { this.deleteById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    isInDatabase.value = false
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onComplete() called")
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onError() called")
                }
            })
    } else {
        Toast.makeText(context, context.getString(R.string.database_item_not_removed), Toast.LENGTH_SHORT).show()
    }
}

fun MyListDao?.isInDatabase(id: Int?, isInDatabase: MutableLiveData<Boolean>) {
    isInDatabase.value = false
    if (id != null && this != null) {
        this.getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            ?.doOnNext {
                isInDatabase.value = true
                Log.d(
                    "log_is_inserted",
                    "ExtensionFunction.IsInDatabase(), getById(), doOnNext called, isInDatabase.value = $isInDatabase"
                )
            }
            ?.subscribe()
    }
}