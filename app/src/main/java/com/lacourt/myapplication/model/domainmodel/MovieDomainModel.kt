package com.lacourt.myapplication.model.domainmodel

data class MovieDomainModel(
    val id: Int,
    val title: String,
//    val images: MovieImages,
    val poster_path: String,
    val backdrop_path: String,
    val isFavourite: Boolean
) {
    // Value object
//    data class MovieImages(
//        val poster_path: String,
//        val backdrop_path: String
//    ) {
//        companion object {
//            val EMPTY = MovieImages("", "")
//        }
//      }
}