package com.lacourt.myapplication.domainmodel

import com.lacourt.myapplication.dto.GenreXDTO

data class Details(
//    val adult: Boolean,
    val backdrop_path: String?,
//    val belongs_to_collectionDTO: BelongsToCollectionDTO,
//    val budget: Int,
    val genres: List<GenreXDTO>,
//    val homepage: String,
    val id: Int,
//    val imdb_id: String,
//    val original_language: String,
//    val original_title: String,
    val overview: String,
//    val popularity: Double,
    val poster_path: String,
//    val production_companyDTOS: List<ProductionCompanyDTO>,
//    val production_countryDTOS: List<ProductionCountryDTO>,
    var release_date: String,
//    val revenue: Int,
//    val runtime: Int,
//    val spoken_languageDTOS: List<SpokenLanguageDTO>,
//    val status: String,
//    val tagline: String,
    val title: String,
//    val video: Boolean,
    val vote_average: Double//,
//    val vote_count: Int
){
    init{
        release_date = release_date.subSequence(0,4).toString()
    }
}
