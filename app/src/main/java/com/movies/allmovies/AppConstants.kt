package com.movies.allmovies

object AppConstants{
    const val JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com"
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_BASE_URL_W185 = "https://image.tmdb.org/t/p/w185/"
    const val TMDB_IMAGE_BASE_URL_W500 = "https://image.tmdb.org/t/p/w500/"
    const val TMDB_IMAGE_BASE_URL_W780 = "https://image.tmdb.org/t/p/w780/"
    const val TMDB_IMAGE_BASE_URL_ORIGINAL = "https://image.tmdb.org/t/p/original/"
    const val TMDB_IMAGE_BASE_URL_W1280 = "https://image.tmdb.org/t/p/w1280/"
    const val LANGUAGE = "en-US"
    const val DATE_ASC = "ASC"
    const val DATE_DESC = "DESC"
    const val VIDEOS = "videos"
    const val VIDEOS_AND_CASTS = "videos,casts"
    const val TMDB_API_KEY = ""

//    DATABASE
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "app_database"

//    ERROR MESSAGES
    const val API_ERROR_MESSAGE = "Resource not found :/\n \"Please, try again later\""
    const val NETWORK_ERROR_MESSAGE = "Network Error :/\n \"Please, check your connection and try again\""
    const val UNKNOWN_ERROR_MESSAGE = "Unexpected Error :/\\n \"Please, check your connection and try again\""
    const val SEARCH_NO_RESULtS = "No results."
}
