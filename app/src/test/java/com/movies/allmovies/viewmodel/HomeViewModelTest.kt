package com.movies.allmovies.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.movies.allmovies.AppConstants
import com.movies.allmovies.domainmappers.toDetails
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.*
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import com.movies.allmovies.repository.HomeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var tmdbApi: TmdbApi

    private val dispatcher = TestCoroutineDispatcher()

    private var homeDataSourceMock: HomeDataSourceMock? = null
    private var viewModel: HomeViewModel? = null

    private lateinit var movieDTOMock: MovieDTO
    private lateinit var successResponseMock: NetworkResponse.Success<MovieResponseDTO>
    private lateinit var resultListsMock: ArrayList<List<MovieDTO>>
    private lateinit var detailsDTOMock: DetailsDTO

    init {
        initialiseMockedObjects()
    }

    @Before
    fun init() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        viewModel = null
        homeDataSourceMock = null
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }
    // method featureName - https://dzone.com/articles/7-popular-unit-test-naming
    // [what it does] if [something happen]
    @Test
    fun `live data lists are filled when data source returns no error`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock = HomeDataSourceMock(
            getListsOfMoviesResponse = NetworkResponse.Success(resultListsMock)
        )
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        // Act
        viewModel?.getListOfMovies()
        // Assert
        assertEquals(resultListsMock, viewModel?.listsOfMovies?.value)
        assertEquals(false, viewModel?.errorScreenVisibility?.value)
        assertEquals(null, viewModel?.errorMessage?.value)
    }

    @Test
    fun `error live data is filled if data source returns api error`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock =
            HomeDataSourceMock(
                getListsOfMoviesResponse = NetworkResponse.ApiError(Error(), 400)
            )
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        // Act
        viewModel?.getListOfMovies()
        // Assert
        assertEquals(null, viewModel?.listsOfMovies?.value)
        assertEquals(true, viewModel?.errorScreenVisibility?.value)
        assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

    @Test
    fun `error live data is filled if data source returns network error`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock =
            HomeDataSourceMock(
                getListsOfMoviesResponse = NetworkResponse.NetworkError(IOException())
            )
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        // Act
        viewModel?.getListOfMovies()
        // Assert
        assertEquals(null, viewModel?.listsOfMovies?.value)
        assertEquals(true, viewModel?.errorScreenVisibility?.value)
        assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

    @Test
    fun `error live data is filled if data source returns unknown error`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock =
            HomeDataSourceMock(
                getListsOfMoviesResponse = NetworkResponse.NetworkError(IOException())
            )
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        // Act
        viewModel?.getListOfMovies()
        // Assert
        assertEquals(null, viewModel?.listsOfMovies?.value)
        assertEquals(true, viewModel?.errorScreenVisibility?.value)
        assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

    @Test
    fun `top trending live data is filled when request returns a successful response`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock = HomeDataSourceMock(
            getListsOfMoviesResponse = NetworkResponse.Success(resultListsMock)
        )
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        `when`(tmdbApi.getDetails(0, AppConstants.LANGUAGE)).thenReturn(
            NetworkResponse.Success(detailsDTOMock)
        )
        // Act
        viewModel?.getListOfMovies()
        // Assert
        // TODO Fix: this should be the exact same instance than the returned one
        assertEquals(detailsDTOMock.toDetails().id, viewModel?.topTrendingMovie?.value?.id)
        assertEquals(detailsDTOMock.toDetails().title, viewModel?.topTrendingMovie?.value?.title)
        assertEquals(false, viewModel?.errorScreenVisibility?.value)
        assertEquals(null, viewModel?.errorMessage?.value)
    }

    @Test
    fun `error live data is filled when request returns a NETWORK error response`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock = HomeDataSourceMock(
            getListsOfMoviesResponse = NetworkResponse.Success(resultListsMock)
        )
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        `when`(tmdbApi.getDetails(0, AppConstants.LANGUAGE)).thenReturn(
            NetworkResponse.NetworkError(IOException())
        )
        // Act
        viewModel?.getListOfMovies()
        // Assert
        assertEquals(null, viewModel?.topTrendingMovie?.value)
        assertEquals(true, viewModel?.errorScreenVisibility?.value)
        assertEquals(AppConstants.NETWORK_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

     @Test
    fun `error live data is filled when request returns a API error response`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock = HomeDataSourceMock(
            getListsOfMoviesResponse = NetworkResponse.Success(resultListsMock)
        )
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        `when`(tmdbApi.getDetails(0, AppConstants.LANGUAGE)).thenReturn(
            NetworkResponse.ApiError(Error(), 400)
        )
        // Act
        viewModel?.getListOfMovies()
        // Assert
        assertEquals(null, viewModel?.topTrendingMovie?.value)
        assertEquals(true, viewModel?.errorScreenVisibility?.value)
        assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

    fun `error live data is filled when request returns a UNKNNOWN error response`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock = HomeDataSourceMock(
            getListsOfMoviesResponse = NetworkResponse.Success(resultListsMock)
        )
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        `when`(tmdbApi.getDetails(0, AppConstants.LANGUAGE)).thenReturn(
            NetworkResponse.UnknownError(Throwable())
        )
        // Act
        viewModel?.getListOfMovies()
        // Assert
        assertEquals(null, viewModel?.topTrendingMovie?.value)
        assertEquals(true, viewModel?.errorScreenVisibility?.value)
        assertEquals(AppConstants.UNKNOWN_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

    private fun initialiseMockedObjects() {
        movieDTOMock = MovieDTO(0, "", "", "")
        successResponseMock = NetworkResponse.Success(MovieResponseDTO(listOf(movieDTOMock)))
        resultListsMock = arrayListOf(
            successResponseMock.body.results,
            successResponseMock.body.results,
            successResponseMock.body.results,
            successResponseMock.body.results
        )
        detailsDTOMock = DetailsDTO(
            false,
            "",
            listOf(GenreXDTO(0,"")),
            0,
            "",
            "",
            "",
            0.0,
            "",
            "2020",
            0,
            "",
            "",
            true,
            0.0,
            VideosDTO(
                arrayListOf(VideoDTO("", "", "", "", "", "", 0, ""))
            ),
            CastsDTO(
                arrayListOf(CastDTO(0, "", "", 0, 0, "", 0, "")),
                // if it doesn't has job = director, the test will fail due to the data manipulation made when converting from DetailsSTO to Details.
                // Although, this logic is useful only for the details screen.
                arrayListOf(CrewDTO("","",0,0,"director","",""))
            )
        )
    }

}

class HomeDataSourceMock(
    private val getListsOfMoviesResponse: NetworkResponse<ArrayList<List<MovieDTO>>, Error>)
    : HomeDataSource {

    override suspend fun getListsOfMovies(
        dispatcher: CoroutineDispatcher,
        homeResultCallback: (result: NetworkResponse<List<List<MovieDTO>>, Error>) -> Unit
    ) {
        homeResultCallback(getListsOfMoviesResponse)
    }

    override fun isTopMovieInDatabase(id: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(myListItem: MyListItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}