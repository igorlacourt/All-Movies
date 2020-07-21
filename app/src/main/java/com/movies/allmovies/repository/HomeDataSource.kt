package com.movies.allmovies.repository

import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.viewmodel.HomeResult

interface HomeDataSource {
    suspend fun getListsOfMovies(homeResultCallback: (result: HomeResult) -> Unit)

    fun isTopMovieInDatabase(id: Int) : Boolean

    fun refresh()

    fun insert(myListItem: MyListItem)

    fun delete(id: Int)
}