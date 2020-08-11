package com.movies.allmovies.repository

import android.content.Context
import android.util.Log
import com.movies.allmovies.AppConstants
import com.movies.allmovies.database.MyListDao
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.network.TmdbApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class HomeDataSourceImpl
    @Inject
    constructor(val context: Context, private val tmdbApi: TmdbApi, private val myListDao: MyListDao)
    : HomeDataSource {

    override suspend fun getListsOfMovies(dispatcher: CoroutineDispatcher, homeResultCallback:  (result: NetworkResponse<List<List<MovieDTO>>, Error>) -> Unit) {
        withContext(dispatcher){
            try {
                val trendingMoviesResponse = async { tmdbApi.getTrendingMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val upcomingMoviesResponse = async { tmdbApi.getUpcomingMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val popularMoviesResponse = async { tmdbApi.getPopularMoviesSuspend(AppConstants.LANGUAGE, 1) }
                val topRatedMoviesResponse = async { tmdbApi.getTopRatedMoviesSuspend(AppConstants.LANGUAGE, 1) }
                processData(
                    homeResultCallback,
                    trendingMoviesResponse.await(),
                    upcomingMoviesResponse.await(),
                    popularMoviesResponse.await(),
                    topRatedMoviesResponse.await()
                )
            } catch (exception: Exception) {
                Log.d("parallelRequest", exception.message)
            }
        }
    }

    override fun isTopMovieInDatabase(id: Int): Boolean {
        return myListDao.exists(id) ?: false
    }

    override fun refresh() {
        //        fetchMoviesLists()
    }

    override fun insert(myListItem: MyListItem) {
        myListDao.insert(myListItem)
    }

    override fun delete(id: Int) {
        myListDao.deleteById(id)
    }

    private fun processData(homeResultCallback: (result: NetworkResponse<List<List<MovieDTO>>, Error>) -> Unit,
                            trending: NetworkResponse<MovieResponseDTO, Error>,
                            upcoming: NetworkResponse<MovieResponseDTO, Error>,
                            popular: NetworkResponse<MovieResponseDTO, Error>,
                            topRated: NetworkResponse<MovieResponseDTO, Error>
    ) {
        val pair1 = buildResponse(trending)
        val pair2 = buildResponse(upcoming)
        val pair3 = buildResponse(popular)
        val pair4 = buildResponse(topRated)

        when {
            pair1.first == null -> {
                pair1.second?.let { homeResultCallback(it) }
                return
            }
            pair2.first == null -> {
                pair2.second?.let { homeResultCallback(it) }
                return
            }
            pair2.first == null -> {
                pair2.second?.let { homeResultCallback(it) }
                return
            }
            pair2.first == null -> {
                pair2.second?.let { homeResultCallback(it) }
                return
            }
            else -> {
                val resultList = ArrayList<List<MovieDTO>>()
                pair1.first?.let { resultList.add(it) }
                pair2.first?.let { resultList.add(it) }
                pair3.first?.let { resultList.add(it) }
                pair4.first?.let { resultList.add(it) }
                homeResultCallback(NetworkResponse.Success(resultList))
            }
        }
    }

    private fun buildResponse(response: NetworkResponse<MovieResponseDTO, Error>)
            : Pair<List<MovieDTO>?, NetworkResponse<List<List<MovieDTO>>, Error>?>
    {
        return when(response) {
            is NetworkResponse.Success -> {
                Pair(response.body.results, null)
            }
            is NetworkResponse.ApiError -> {
                Pair(null, NetworkResponse.ApiError(response.body, response.code))
            }
            is NetworkResponse.NetworkError -> {
                Pair(null, NetworkResponse.NetworkError(IOException()))
            }
            is NetworkResponse.UnknownError -> {
                Pair(null, NetworkResponse.UnknownError(Throwable()))
            }
        }
    }
}
