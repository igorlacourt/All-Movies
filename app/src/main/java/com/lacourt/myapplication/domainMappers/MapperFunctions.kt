package com.lacourt.myapplication.domainMappers

import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.dto.MovieDTO

object MapperFunctions {

    fun toDetails(input: DetailsDTO): Details {
        return with(input) {
            Details(
                backdrop_path,
                genres,
                id,
                overview,
                poster_path,
                release_date,
                title,
                vote_average
            )
        }
    }

    fun toMyListItem(input: Details): MyListItem {
        return with(input) {
            MyListItem(
                id,
                poster_path,
                release_date,
                vote_average
            )
        }
    }

    fun toListOfDbMovieDTO(networkMoviesList: List<MovieDTO>): List<DbMovieDTO> {
        return networkMoviesList.map {
            with(it){
                DbMovieDTO(
                    id,
                    poster_path,
                    release_date,
                    vote_average
                )
            }
        }
    }

    fun toDbMovieDTO(input: MovieDTO): DbMovieDTO {
        return with(input){
            DbMovieDTO(id, poster_path, release_date, vote_average)
        }
    }
}