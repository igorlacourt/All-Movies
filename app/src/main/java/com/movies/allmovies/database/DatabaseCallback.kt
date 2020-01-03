package com.movies.allmovies.database

interface DatabaseCallback {
    fun onItemDeleted()
    fun onDetailsLoaded()
}