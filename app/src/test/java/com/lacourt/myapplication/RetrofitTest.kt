package com.lacourt.myapplication

import android.os.AsyncTask.execute
import com.lacourt.myapplication.network.Apifactory.tmdbApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.IOException


class RetrofitTest {

    @Test
    fun `chamada rertofit codigo de resposta true`() {

        val call = tmdbApi.getMovies(AppConstants.LANGUAGE, 1)

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
