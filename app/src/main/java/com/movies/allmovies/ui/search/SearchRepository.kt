package com.movies.allmovies.ui.search

import com.movies.allmovies.dto.MovieDTO
import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Resource
import com.movies.allmovies.repository.NetworkResponse
import com.movies.allmovies.repository.SearchResult

interface SearchRepository {
 fun searchMovie(title: String, searchResultCallback: (result: SearchResult) -> Unit)
 suspend fun requestMovie(title: String) : Resource<MovieResponseDTO>
}