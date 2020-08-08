package com.movies.allmovies.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.movies.allmovies.AppConstants
import com.movies.allmovies.domainmappers.toDomainMovieList
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import com.movies.allmovies.viewmodel.HomeViewModel
import junit.framework.Assert.assertEquals
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
class HomeViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var tmdbApi: TmdbApi

    private val dispatcher = TestCoroutineDispatcher()

    private var homeDataSource: HomeDataSourceImpl? = null
    private var viewModel: HomeViewModel? = null
    private val movieDTOMock = MovieDTO(false, "", arrayListOf("0","0","0"), arrayListOf("","",""), 0, "", "", "", 0.0, "", "", "", false, 0.0, 0)
    private val successResponseMock = NetworkResponse.Success(MovieResponseDTO(listOf(movieDTOMock)))
    private val resultListsMock = arrayListOf(
        successResponseMock.body.toDomainMovieList(),
        successResponseMock.body.toDomainMovieList(),
        successResponseMock.body.toDomainMovieList(),
        successResponseMock.body.toDomainMovieList()
    )

    @Before
    fun init() {
        homeDataSource = HomeDataSourceImpl(context, tmdbApi)
        viewModel = HomeViewModel(context, tmdbApi, homeDataSource!!, dispatcher)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        viewModel = null
        homeDataSource = null
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    // title should have: given, when, then
    @Test
    fun `make all 4 requests and all of them is successful`() = dispatcher.runBlockingTest {
        // Arrange
        `when`(tmdbApi.getTrendingMoviesSuspend(AppConstants.LANGUAGE, 1)).thenReturn(
            successResponseMock
        )
        `when`(tmdbApi.getUpcomingMoviesSuspend(AppConstants.LANGUAGE, 1)).thenReturn(
            successResponseMock
        )
        `when`(tmdbApi.getPopularMoviesSuspend(AppConstants.LANGUAGE, 1)).thenReturn(
            successResponseMock
        )
        `when`(tmdbApi.getTopRatedMoviesSuspend(AppConstants.LANGUAGE, 1)).thenReturn(
            successResponseMock
        )
        // Act
        viewModel?.getListOfMovies()
        // Assert
        Assert.assertEquals(resultListsMock, viewModel?.listsOfMovies?.value)
        assertEquals(false, viewModel?.errorScreenVisibility?.value)
        assertEquals(null, viewModel?.errorMessage?.value)
    }
}