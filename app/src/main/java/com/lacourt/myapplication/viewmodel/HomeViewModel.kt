package com.lacourt.myapplication.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.AppConstants.DATE_ASC
import com.lacourt.myapplication.AppConstants.DATE_DESC
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.indlingresource.IdlingResoureManager
import com.lacourt.myapplication.model.Genre
import com.lacourt.myapplication.model.GenreResponse
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.MovieResponse
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.network.Apifactory.tmdbApi
import com.lacourt.myapplication.repository.Repository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: Repository? = null
    var movies: LiveData<PagedList<Movie>>? = null

    init {
        repository = Repository(application)
        movies = repository?.movies
    }

    fun rearrengeMovies(order: String) {
        repository?.rearrengeMovies(order)
    }
}