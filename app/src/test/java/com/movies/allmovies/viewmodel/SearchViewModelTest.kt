package com.movies.allmovies.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.movies.allmovies.AppConstants
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var tmdbApi: TmdbApi

    private val dispatcher = TestCoroutineDispatcher()
    private var viewModel: SearchViewModel? = null
    private val movieTitle = "nemo"
    private val successResponse = NetworkResponse.Success(
        MovieResponseDTO(
            listOf(MovieDTO(0, "", "", ""))
        )
    )

    private val networkErrorResponse = NetworkResponse.NetworkError(IOException())
    private val apiErrorResponse = NetworkResponse.ApiError(Error(500, "unavailable"), 500)
    private val unknownErrorResponse = NetworkResponse.UnknownError(Throwable())

    @Before
    fun init() {
        viewModel = SearchViewModel(tmdbApi, dispatcher)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        viewModel = null
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `make request, returns 200`() = dispatcher.runBlockingTest {
        //Arrange
        `when`(tmdbApi.searchMovie(AppConstants.LANGUAGE, movieTitle, false)).thenReturn(
            successResponse
        )
        //Act
        viewModel?.searchMovie(movieTitle)

        //Assert
        assertEquals(null, viewModel?.errorMessage?.value)
        assertEquals(false, viewModel?.errorScreenVisibility?.value)
        assertEquals(successResponse.body.results, viewModel?.searchResult?.value)
    }

    @Test
    fun `make request, returns 400`() = dispatcher.runBlockingTest {
        //Arrange
        `when`(tmdbApi.searchMovie(AppConstants.LANGUAGE, movieTitle, false)).thenReturn(
            networkErrorResponse
        )
        //Act
        viewModel?.searchMovie(movieTitle)

        //Assert
        assertEquals(null, viewModel?.searchResult?.value)
        assertEquals(true, viewModel?.errorScreenVisibility?.value)
        assertEquals(AppConstants.NETWORK_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }


    @Test
    fun `make request, returns 500`() = dispatcher.runBlockingTest {
        // Arrange
        `when`(tmdbApi.searchMovie(AppConstants.LANGUAGE, movieTitle, false)).thenReturn(
            apiErrorResponse
        )
        //Act
        viewModel?.searchMovie(movieTitle)

        //Assert
        assertEquals(null, viewModel?.searchResult?.value)
        assertEquals(true, viewModel?.errorScreenVisibility?.value)
        assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

}