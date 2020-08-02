package com.movies.allmovies.viewmodel

import com.movies.allmovies.AppConstants
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.json.JSONObject
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response
import java.io.IOException
import com.movies.allmovies.network.Error

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
        Assert.assertEquals(null, viewModel?.apiErrorResult?.value)
        Assert.assertEquals(null, viewModel?.unknownErrorResult?.value)
        Assert.assertEquals(networkErrorResponse.error, viewModel?.networkErrorResult?.value)
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
        Assert.assertEquals(null, viewModel?.apiErrorResult?.value)
        Assert.assertEquals(null, viewModel?.unknownErrorResult?.value)
        Assert.assertEquals(networkErrorResponse.error, viewModel?.networkErrorResult?.value)
    }
}