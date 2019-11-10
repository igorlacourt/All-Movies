package com.lacourt.myapplication.domainMappers

import com.lacourt.myapplication.domainmodel.DomainDetails
import com.lacourt.myapplication.domainmodel.DomainMovie
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

fun DomainDetails.toMyListItem(): MyListItem {
    return with(this) {
        MyListItem(
            id,
            poster_path,
            release_date,
            vote_average
        )
    }
}

fun MovieResponseDTO.toDomiainMovie(): List<DomainMovie> {
    return this.results.map { movieDTO ->
        DomainMovie(
            movieDTO.id,
            movieDTO.poster_path
        )

    }
}

fun List<MyListItem>.toDomiainMovie(): List<DomainMovie> {
    return this.map { movieDTO ->
        DomainMovie(
            movieDTO.id,
            movieDTO.poster_path
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

    fun toDetails(input: DetailsDTO): DomainDetails {
        return with(input) {
            DomainDetails(
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


//    fun toMyListItem(input: DomainDetails): MyListItem {
//        return with(input) {
//            MyListItem(
//                id,
//                poster_path,
//                release_date,
//                vote_average
//            )
//        }
//    }

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