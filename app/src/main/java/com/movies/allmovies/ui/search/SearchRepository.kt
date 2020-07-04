package com.movies.allmovies.ui.search

import com.movies.allmovies.repository.SearchResult

interface SearchRepository {
 fun searchMovie(title: String, searchResultCallback: (result: SearchResult) -> Unit)
}