package com.movies.allmovies.viewmodel

import com.movies.allmovies.AppConstants
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(InstantExecutorExtension::class)
class SearchViewModelTest {
    private val dispatcher = TestCoroutineDispatcher()
    private val tmdbApi = mockk<TmdbApi>()
    private var viewModel: SearchViewModel? = SearchViewModel(tmdbApi, dispatcher)
    private val movieTitle = "nemo"
    private val successResponse = NetworkResponse.Success(
        MovieResponseDTO(listOf(
            MovieDTO(
                false,
                "backdrop-path",
                arrayListOf("1", "2", "3", "4"),
                arrayListOf("comedy, drama, adventure"),
                1234,
                "en-US",
                "Finding Nemo",
                "movie overview",
                90.0,
                "poster-path",
                "2020",
                "Finding Nemo",
                false,
                10.0,
                200000
            )
        ))
    )

    private val networkErrorResponse = NetworkResponse.NetworkError(IOException())
    private val apiErrorResponse = NetworkResponse.ApiError(Error(500, "unavailable"), 500)
    private val unknownErrorResponse = NetworkResponse.UnknownError(Throwable())

    @BeforeEach
    fun init() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        viewModel = null
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `make request, returns 200`() {
        //Arrange
        coEvery { tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, movieTitle, false) } answers {
            successResponse
        }
        //Act
        viewModel?.searchMovie(movieTitle)

        //Assert
        Assert.assertEquals(null, viewModel?.errorMessage?.value)
        Assert.assertEquals(false, viewModel?.errorScreenVisibility?.value)
        Assert.assertEquals(successResponse.body.results, viewModel?.searchResult?.value)
    }

    @Test
    fun `make request, returns 400`() {
        //Arrange
        coEvery { tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, movieTitle, false) } answers {
            networkErrorResponse
        }
        //Act
        viewModel?.searchMovie(movieTitle)

        //Assert
        Assert.assertEquals(null, viewModel?.searchResult?.value)
        Assert.assertEquals(true, viewModel?.errorScreenVisibility?.value)
        Assert.assertEquals(AppConstants.NETWORK_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

    @Test
    fun `make request, returns 500`() {
        //Arrange
        coEvery { tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, movieTitle, false) } answers {
            apiErrorResponse
        }
        //Act
        viewModel?.searchMovie(movieTitle)


        //Assert
        Assert.assertEquals(null, viewModel?.searchResult?.value)
        Assert.assertEquals(true, viewModel?.errorScreenVisibility?.value)
        Assert.assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }
}