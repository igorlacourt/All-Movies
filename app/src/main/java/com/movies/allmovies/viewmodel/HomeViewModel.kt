package com.movies.allmovies.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.movies.allmovies.AppConstants
import com.movies.allmovies.database.AppDatabase
import com.movies.allmovies.domainMappers.MapperFunctions
import com.movies.allmovies.domainMappers.toDomainMovie
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Apifactory.tmdbApi
import com.movies.allmovies.network.Error
import com.movies.allmovies.repository.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface HomeDataSource {
    suspend fun getListsOfMovies(homeResultCallback: (result: HomeResult) -> Unit)

    fun isTopMovieInDatabase(id: Int) : Boolean

    fun refresh()

    fun insert(myListItem: MyListItem)

    fun delete(id: Int)
}

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
        return myListDao?.getById(id) ?: false
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

class HomeViewModel @Inject constructor(private val homeDataSource: HomeDataSource, val context: Context) : ViewModel(){
    var topTrendingMovie: MutableLiveData<Details>? = MutableLiveData()
    var listsOfMovies: MutableLiveData<List<Collection<DomainMovie>>?> = MutableLiveData()

    var isInDatabase: MutableLiveData<Boolean> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {

        isLoading.value = true
        getListOfMovies()
    }

    private fun getListOfMovies() {
        viewModelScope.launch{
            homeDataSource.getListsOfMovies{ result ->
                when(result) {
                    is HomeResult.Success -> {
                        Log.d("searchlog", "searchMovie, SearchResult.Success")
                        listsOfMovies.postValue(result.movies)
                        getTopMovie(result.movies[0].elementAt(0).id)
                    }
                    is HomeResult.ApiError -> {
                        Log.d("searchlog", "searchMovie, SearchResult.ApiError")
                        isLoading.postValue(false)
                    }
                    is HomeResult.ServerError -> {
                        Log.d("searchlog", "searchMovie, SearchResult.ServerError")
                        isLoading.postValue(false)
                    }

                }
            }
        }
    }

    private fun getTopMovie(id: Int?) {
        viewModelScope.launch {
            try {
                val response = tmdbApi.getDetails(id, AppConstants.LANGUAGE)
                when (response) {
                    is NetworkResponse.Success -> {
                        topTrendingMovie?.postValue(MapperFunctions.toDetails(response.body))
                    }
                    is NetworkResponse.ApiError -> Log.d("getTopMovie", "ApiError ${response.body.message}")
                    is NetworkResponse.NetworkError -> Log.d("getTopMovie", "NetworkError")
                    is NetworkResponse.UnknownError -> Log.d("getTopMovie", "UnknownError")
                }
            } catch (e: Exception) {
                Log.d("parallelRequest", e.message)
                throw e
            }
        }
    }

    fun isTopMovieInDatabase(id: Int){
        isInDatabase.value = homeDataSource.isTopMovieInDatabase(id)
        isLoading.value = false
    }

    fun refresh(){
        homeDataSource.refresh()
    }

    fun insert(myListItem: MyListItem) {
        homeDataSource.insert(myListItem)
        isInDatabase.value = true
    }

    fun delete(id: Int){
        homeDataSource.delete(id)
        isInDatabase.value = false
    }

}
sealed class HomeResult {
    class Success(val movies: ArrayList<Collection<DomainMovie>>) : HomeResult()
    class ApiError(val statusCode: Int) : HomeResult()
    object ServerError : HomeResult()
}