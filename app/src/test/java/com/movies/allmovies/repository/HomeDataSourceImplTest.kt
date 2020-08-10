package com.movies.allmovies.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.movies.allmovies.AppConstants
import com.movies.allmovies.network.TmdbApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeDataSourceImplTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var tmdbApi: TmdbApi

    @Before
    fun init() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }
    @Test
    fun `make all 4 requests and all of them is successful`() = dispatcher.runBlockingTest {
        // Arrange
//        `when`(tmdbApi.getTrendingMoviesSuspend(AppConstants.LANGUAGE, 1)).thenReturn(
//            successResponseMock
//        )
//        `when`(tmdbApi.getUpcomingMoviesSuspend(AppConstants.LANGUAGE, 1)).thenReturn(
//            successResponseMock
//        )
//        `when`(tmdbApi.getPopularMoviesSuspend(AppConstants.LANGUAGE, 1)).thenReturn(
//            successResponseMock
//        )
//        `when`(tmdbApi.getTopRatedMoviesSuspend(AppConstants.LANGUAGE, 1)).thenReturn(
//            successResponseMock
//        )
        // Act
//        viewModel?.getListOfMovies()
        // Assert
//        Assert.assertEquals(resultListsMock, viewModel?.listsOfMovies?.value)
//        Assert.assertEquals(false, viewModel?.errorScreenVisibility?.value)
//        Assert.assertEquals(null, viewModel?.errorMessage?.value)
    }
}