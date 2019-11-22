package com.lacourt.myapplication

import android.os.AsyncTask.execute
import android.util.Log
import android.widget.Toast
import com.lacourt.myapplication.dto.MovieResponseDTO
import com.lacourt.myapplication.network.Apifactory
import com.lacourt.myapplication.network.Apifactory.tmdbApi
import com.lacourt.myapplication.network.TmdbApi
import com.lacourt.myapplication.repository.HomeRepository
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.IOException


class RetrofitTest {

    @Test
    fun `When get example shirts then shirts are retrieved`() {

        val testSubscriber = TestSubscriber<MovieResponseDTO>()
        val exampleSubscription =
        Completable.fromAction { Apifactory.tmdbApi.getPopularMovies(AppConstants.LANGUAGE, 1) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( object : CompletableObserver {
                override fun onComplete() {

                    Log.d("log_is_inserted", "DetailsRepository, delete(), onComplete() called")
                    Log.d("mylistclick", "ExtensionFunctions, deleteById(), onComplete() called")

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("log_is_inserted", "DetailsRepository, delete(), onError() called")
                    Log.d("mylistclick", "ExtensionFunctions, deleteById(), onError() called")

                }
            })
            .subscribe(testSubscriber)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
    }

    @Test
    fun `chamada rertofit codigo de resposta true`() {

        val call = tmdbApi.getPopularMovies(AppConstants.LANGUAGE, 1)

        try {
            //Magic is here at .execute() instead of .enqueue()
            val response = call.execute()
            val isSizeOk = response.body()!!.results.size == 20

            assertTrue(response.isSuccessful() && isSizeOk)
//            assertThat(response.code(), `is`(200))

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun movies_call_mockito(){
//        Mockito.verify(tmdbApi).getMovies(Mockito.anyString(), cb.capture())
//
//        val testRepos = ArrayList<HomeRepository>()
//        testRepos.add(HomeRepository("rails", "ruby", Owner("dhh")))
//        testRepos.add(HomeRepository("android", "java", Owner("google")))
//
//        cb.getValue().success(testRepos, null)
//
//        assertThat(activity.getListAdapter()).hasCount(2)

    }
}
