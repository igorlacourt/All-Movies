package com.lacourt.myapplication.domainMappers

import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.dto.DetailsDTO
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.dto.MovieResponseDTO

fun MovieResponseDTO.mapToDomain() : Collection<DbMovieDTO> {
    return this.results.map { movieDTO ->
        DbMovieDTO(
            movieDTO.id,
            movieDTO.poster_path,
            movieDTO.release_date,
            movieDTO.vote_average
        )
    }
}

object MapperFunctions {

    fun movieResponseToDbMovieDTO(input: MovieResponseDTO): List<DbMovieDTO> {
        return input.results.map { movieDTO ->
            DbMovieDTO(
                movieDTO.id,
                movieDTO.poster_path,
                movieDTO.release_date,
                movieDTO.vote_average
            )

        }
    }

    fun toDetails(input: DetailsDTO): Details {
        return with(input) {
            Details(
                backdrop_path,
                genres,
                id,
                overview,
                poster_path,
                release_date,
                runtime,
                title,
                vote_average,
                videos?.results
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
            with(it) {
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
        return with(input) {
            DbMovieDTO(id, poster_path, release_date, vote_average)
        }
    }
}