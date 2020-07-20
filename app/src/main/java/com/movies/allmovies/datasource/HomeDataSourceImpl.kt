package com.movies.allmovies.datasource

import android.content.Context
import android.util.Log
import com.movies.allmovies.AppConstants
import com.movies.allmovies.database.AppDatabase
import com.movies.allmovies.domainmappers.toDomainMovie
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Apifactory.tmdbApi
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.NetworkResponse
import com.movies.allmovies.viewmodel.HomeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(val context: Context): HomeDataSource {
    override suspend fun getListsOfMovies(homeResultCallback: (result: HomeResult) -> Unit) {
        withContext(Dispatchers.IO){
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

    private val myListDao =
        AppDatabase.getDatabase(context)?.MyListDao()

    override fun isTopMovieInDatabase(id: Int): Boolean {
        return myListDao?.exists(id) ?: false
    }

    override fun refresh() {
        //        fetchMoviesLists()
    }

    override fun insert(myListItem: MyListItem) {
        myListDao?.insert(myListItem)
    }

    override fun delete(id: Int) {
        myListDao?.deleteById(id)
    }

    private fun processData(homeResultCallback: (result: HomeResult) -> Unit,
                            trending: NetworkResponse<MovieResponseDTO, Error>,
                            upcoming: NetworkResponse<MovieResponseDTO, Error>,
                            popular: NetworkResponse<MovieResponseDTO, Error>,
                            topRated: NetworkResponse<MovieResponseDTO, Error>
    ) {
        val list1 = convertResponse(trending)
        val list2 = convertResponse(upcoming)
        val list3 = convertResponse(popular)
        val list4 = convertResponse(topRated)

        val resultList = ArrayList<Collection<DomainMovie>>()
        list1?.let { resultList.add(it) }
        list2?.let { resultList.add(it) }
        list3?.let { resultList.add(it) }
        list4?.let { resultList.add(it) }

        if (resultList.size == 4){
            homeResultCallback(HomeResult.Success(resultList))
        } else {
            homeResultCallback(HomeResult.ApiError(400))
        }
    }

    private fun convertResponse(response: NetworkResponse<MovieResponseDTO, Error>): Collection<DomainMovie>? {
        when(response){
            is NetworkResponse.Success -> {
                return response.body.toDomainMovie()
            }
            is NetworkResponse.ApiError -> {
                Log.d("TAG", "ApiError ${response.body}")
                return null
            }
            is NetworkResponse.NetworkError -> {
                Log.d("TAG", "NetworkError")
                return null
            }
            is NetworkResponse.UnknownError -> {
                Log.d("TAG", "UnknownError")
                return null
            }
        }
    }
}
