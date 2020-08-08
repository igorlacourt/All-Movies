package com.movies.allmovies.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.movies.allmovies.AppConstants
import com.movies.allmovies.domainmappers.toDomainMovieList
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import com.movies.allmovies.viewmodel.HomeResult
import com.movies.allmovies.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
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

    private var homeDataSourceMock: HomeDataSourceMock? = null
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
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        viewModel = null
        homeDataSourceMock = null
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    // title should have: given, when, then
    @Test
    fun `get lists of movies from the data source, and all lists come with no error`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock = HomeDataSourceMock(resultListsMock, success = true)
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        // Act
        viewModel?.getListOfMovies()
        // Assert
        Assert.assertEquals(resultListsMock, viewModel?.listsOfMovies?.value)
        Assert.assertEquals(false, viewModel?.errorScreenVisibility?.value)
        Assert.assertEquals(null, viewModel?.errorMessage?.value)
    }

    @Test
    fun `get lists of movies from the data source, but an error occurs in one or more of the requests`() = dispatcher.runBlockingTest {
        // Arrange
        homeDataSourceMock = HomeDataSourceMock(success = false)
        viewModel = HomeViewModel(context, tmdbApi, homeDataSourceMock!!, dispatcher)
        // Act
        viewModel?.getListOfMovies()
        // Assert
        Assert.assertEquals(null, viewModel?.listsOfMovies?.value)
        Assert.assertEquals(true, viewModel?.errorScreenVisibility?.value)
        Assert.assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel?.errorMessage?.value)
    }

//    @Test
//    fun `make all 4 requests and all of them is successful`() = dispatcher.runBlockingTest {
//        // Arrange
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
//        // Act
//        viewModel?.getListOfMovies()
//        // Assert
//        Assert.assertEquals(resultListsMock, viewModel?.listsOfMovies?.value)
//        assertEquals(false, viewModel?.errorScreenVisibility?.value)
//        assertEquals(null, viewModel?.errorMessage?.value)
//    }
}

class HomeDataSourceMock(private val resultLists: ArrayList<Collection<DomainMovie>>? = null, private val success: Boolean) : HomeDataSource {
    override suspend fun getListsOfMovies(dispatcher: CoroutineDispatcher, homeResultCallback: (result: HomeResult) -> Unit) {
        if(success){
            resultLists?.let { homeResultCallback(HomeResult.Success(resultLists)) }
        } else {
            homeResultCallback(HomeResult.ApiError(400))
        }
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