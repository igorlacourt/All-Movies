package com.lacourt.myapplication

import com.lacourt.myapplication.network.Apifactory
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun moviesRequest_checkNumberOfItems() {
        val result = Apifactory.tmdbApi.getMovies(AppConstants.LANGUAGE, 1).execute()

        if(result.isSuccessful) {
            val moviesCount = result.body()?.results?.size
            assertThat(moviesCount, `is`(20))
        } else {
            AssertionFailedError("Request fail - ${result.code()}")
        }
    }
}
