package com.movies.allmovies.ui.search

import com.movies.allmovies.dto.MovieResponseDTO
import com.movies.allmovies.network.Resource
import com.movies.allmovies.network.new_network.SearchResult


interface SearchRepository {
 fun searchMovie(title: String, searchResultCallback: (result: SearchResult) -> Unit)
 suspend fun requestMovie(title: String) : Resource<MovieResponseDTO>
}