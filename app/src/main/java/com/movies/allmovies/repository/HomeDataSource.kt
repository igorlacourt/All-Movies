package com.movies.allmovies.repository

import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.network.Error
import com.movies.allmovies.network.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher

interface HomeDataSource {
    suspend fun getListsOfMovies(dispatcher: CoroutineDispatcher, homeResultCallback: (result: NetworkResponse<List<List<MovieDTO>>, Error>) -> Unit)

    fun isTopMovieInDatabase(id: Int) : Boolean

    fun refresh()

    fun insert(myListItem: MyListItem)

    fun delete(id: Int)
}