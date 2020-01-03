package com.movies.allmovies.domainMappers

import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.*

fun Details.toMyListItem(): MyListItem? {
    return with(this) {
        MyListItem(
            id,
            poster_path,
            release_date,
            vote_average
        )
    }

}

fun List<MyListItem>.toDomainMovie(): List<DomainMovie>? {
    return this.map { myListItem ->
        DomainMovie(
            myListItem.id,
            myListItem.poster_path
        )
    }

}

fun MovieResponseDTO.toDomainMovie(): Collection<DomainMovie> {
    return this.results.map { movieDTO ->
        DomainMovie(
            movieDTO.id,
            movieDTO.poster_path
        )
    }
}

fun MovieResponseDTO.mapToDomain(): Collection<DbMovieDTO> {
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
                videos?.results,
                casts
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