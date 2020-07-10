package com.movies.allmovies.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.movies.allmovies.AppConstants
import com.movies.allmovies.domainMappers.toDomainMovie
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Apifactory
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.Resource
import com.movies.allmovies.repository.HomeRepository
import com.movies.allmovies.repository.HomeResult
import com.movies.allmovies.repository.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface HomeDataSource {
    suspend fun getListsOfMovies(homeResultCallback: (result: HomeResult) -> Unit)
}

class HomeDataSourceImpl @Inject constructor(): HomeDataSource {
    override suspend fun getListsOfMovies(homeResultCallback: (result: HomeResult) -> Unit) {
        val tmdbApi = Apifactory.tmdbApi
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

    private fun processData(homeResultCallback: (result: HomeResult) -> Unit,
                            trending: NetworkResponse<MovieResponseDTO, Error>,
                            upcoming: NetworkResponse<MovieResponseDTO, Error>,
                            popular: NetworkResponse<MovieResponseDTO, Error>,
                            topRated: NetworkResponse<MovieResponseDTO, Error>
    ) {
        val list1: Collection<DomainMovie>? = convertResponse(trending)
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

    private fun convertResponse(trending: NetworkResponse<MovieResponseDTO, Error>): Collection<DomainMovie>? {
        when(trending){
            is NetworkResponse.Success -> {
                return trending.body.toDomainMovie()
            }
            is NetworkResponse.ApiError -> {
                Log.d("TAG", "ApiError ${trending.body}")
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

class HomeViewModel @Inject constructor(homeDataSource: HomeDataSource, val context: Context) : ViewModel(){
    var app = context

    private var repository: HomeRepository? = null
    var topTrendingMovie: LiveData<Resource<Details>>? = null
    var listsOfMovies: MutableLiveData<List<Collection<DomainMovie>>?> = MutableLiveData()

    var isInDatabase: LiveData<Boolean>? = null
    var isLoading: MutableLiveData<Boolean>? = MutableLiveData()

    init {
        repository = HomeRepository(app as Application)

        viewModelScope.launch{
            homeDataSource.getListsOfMovies{ result ->
                when(result) {
                    is HomeResult.Success -> {
                    Log.d("searchlog", "searchMovie, SearchResult.Success")
                        listsOfMovies.postValue(result.movies)
                        isLoading?.postValue(false)
                    }
                    is HomeResult.ApiError -> {
                      Log.d("searchlog", "searchMovie, SearchResult.ApiError")
                        isLoading?.postValue(false)
                    }
                    is HomeResult.ServerError -> {
                      Log.d("searchlog", "searchMovie, SearchResult.ServerError")
                        isLoading?.postValue(false)
                    }

                }
            }
        }

        topTrendingMovie = repository?.topTrendingMovie

        isInDatabase = repository?.isInDatabase
    }

    fun isIndatabase(id: Int?){
        repository?.isInDatabase(id)
    }

    fun refresh(){
//        Log.d("refresh", "HomeViewMmodel, refresh()")
        repository?.refresh()
//        Log.d("listsLog", "HomeViewModdel, resultList.size = ${listsOfMovies?.value?.data?.size}")
    }

    fun insert(myListItem: MyListItem) {
        Log.d("log_is_inserted", "HomeViewModel, insert() called")
        repository?.insert(myListItem)
    }

    fun delete(id: Int){
//        Log.d("log_is_inserted", "HomeViewModel, delete() called")
        repository?.delete(id)
    }

}