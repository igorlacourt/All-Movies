package com.movies.allmovies.domainmappers

import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.domainmodel.MyListItem
import com.movies.allmovies.dto.*

fun Details.toMyListItem(): MyListItem {
    return with(this) {
        MyListItem(
            id,
            poster_path,
            release_date
        )
    }

}

fun ArrayList<CrewDTO>.toCastDTO(): List<CastDTO> {
    return this.map { crewDTO ->
        CastDTO(
            id = crewDTO.id,
            name = crewDTO.name,
            character = crewDTO.job,
            creditId = crewDTO.creditId,
            gender = crewDTO.gender,
            castId = null,
            order = null,
            profilePath = crewDTO.profilePath
        )
    }
}

fun List<MyListItem>.toMovieList(): List<MovieDTO> {
    return this.map { myListItem ->
        MovieDTO(
            myListItem.id,
            myListItem.poster_path,
            myListItem.backdrop_path,
            null
        )
    }
}

fun DetailsDTO.toDetails(): Details {
    return with(this) {
        Details(
            backdrop_path,
            genres,
            id,
            overview,
            poster_path,
            release_date,
            runtime.toString(),
            title,
            vote_average,
            videos?.results,
            casts
        )
    }
}