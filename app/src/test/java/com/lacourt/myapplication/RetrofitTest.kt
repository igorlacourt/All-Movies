package com.lacourt.myapplication

import com.lacourt.myapplication.dto.MovieResponseDTO
import com.lacourt.myapplication.network.Apifactory
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertTrue
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.model.Statement
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class RetrofitTest {
    private val LION_KING_ID = 420818

    @Rule
    @JvmField
    var testSchedulerRule = RxTrampolineSchedulerRule()

    @Before
    fun setUp() {
    }


    @Test
    fun `When get top rated movies, then movies response object is retrieved`() {
        val testObservable = TestObserver<MovieResponseDTO>()

        Apifactory.tmdbApi.getTopRatedMovies(AppConstants.LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(testObservable)

        testObservable.assertSubscribed()
        testObservable.assertComplete()
        testObservable.assertNoErrors()
    }

    @Test
    fun `When get trending movies, then movies response object is retrieved`() {
        val testObservable = TestObserver<MovieResponseDTO>()

        Apifactory.tmdbApi.getTrendingMovies(AppConstants.LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(testObservable)

        testObservable.assertSubscribed()
        testObservable.assertComplete()
        testObservable.assertNoErrors()
    }

    @Test
    fun `When movie is searched, then movies response object is retrieved`() {
        val call =
            Apifactory.tmdbApi.searchMovie(AppConstants.LANGUAGE, "emperror's new groove", false)
        try {
            val response = call.execute()
            assertTrue(response.isSuccessful)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun `When get recommendations for a movie, then movies response object is retrieved`() {
        val testObservable = TestObserver<MovieResponseDTO>()

        Apifactory.tmdbApi.getRecommendations(LION_KING_ID, AppConstants.LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(testObservable)

        testObservable.assertSubscribed()
        testObservable.assertComplete()
        testObservable.assertNoErrors()
    }

    @Test
    fun `When get similar movies, then movies response object is retrieved`() {
        val testObservable = TestObserver<MovieResponseDTO>()

        Apifactory.tmdbApi.getSimilar(LION_KING_ID, AppConstants.LANGUAGE, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(testObservable)

        testObservable.assertSubscribed()
        testObservable.assertComplete()
        testObservable.assertNoErrors()
    }

    @Test
    fun `When get movie's details, then movies response object is retrieved`() {
        val call = Apifactory.tmdbApi.getDetails(LION_KING_ID, AppConstants.VIDEOS_AND_CASTS)

        try {
            val response = call.execute()
            assertTrue(response.isSuccessful)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
//    assertThat(response.code(), `is`(200))


    @Test
    fun movies_call_mockito() {
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

