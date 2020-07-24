package com.movies.allmovies.viewmodel

import com.movies.allmovies.AppConstants
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

//@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class SearchViewModelTest {
//    private val dispathcer = TestCoroutinesDispatcher()

    private val tmdbApi = mockk<TmdbApi>()

    private var viewModel: SearchViewModel? = SearchViewModel(tmdbApi)
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

    @BeforeEach
    fun init() {

    }

    @AfterEach
    fun tearDown() {
        viewModel = null
//        Dicspatchers.reseMain()
//        testDispatecher.cleanupTestCoroutines()
    }

    @Test
    fun `make reques, retorno 200`(){
        //Arrange
        coEvery { tmdbApi.searchMovieSuspend(AppConstants.LANGUAGE, movieTitle, false) } answers {
            successResponse
        }
        //Act
        viewModel?.searchMovie(movieTitle)

        //Assert
        Assert.assertEquals(successResponse, viewModel?.searchResult)
    }
}