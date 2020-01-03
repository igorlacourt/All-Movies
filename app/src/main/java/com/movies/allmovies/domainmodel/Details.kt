package com.movies.allmovies.domainmodel

import android.util.Log
import com.movies.allmovies.dto.CastsDTO
import com.movies.allmovies.dto.CrewDTO
import com.movies.allmovies.dto.GenreXDTO
import com.movies.allmovies.dto.VideoDTO
import kotlin.collections.ArrayList

data class Details(
    val backdrop_path: String?,
    val genres: List<GenreXDTO>?,
    val id: Int?,
    val overview: String?,
    val poster_path: String?,
    var release_date: String?,
    var runtime: Int?,
    val title: String?,
    val vote_average: Double?,
    val videos: ArrayList<VideoDTO>?,
    val casts: CastsDTO?
) {
    init {
        release_date = release_date?.subSequence(0, 4).toString()

        val trailer = getTrailer()
        videos?.clear()
        trailer?.let { videos?.add(it) }

       formatCasts()
    }

    fun formatCasts() {
        if (!casts?.cast.isNullOrEmpty()){
            for (i in casts?.cast!!.size - 1 downTo 3){
                casts.cast.removeAt(i)
            }
        }

        var director: CrewDTO? = null
        if(!casts?.crew.isNullOrEmpty()){
            casts?.crew?.forEach { crew ->
                var job = crew.job?.toLowerCase()
                if (director == null && job == "director" ) {
                    director = crew
                    Log.d("dirlog", "$job == \"director\", is director, ${crew.job?.toLowerCase()}")
                } else {
                    Log.d("dirlog", "$job == \"director\", not director, ${crew.job?.toLowerCase()}")
                }
            }
            casts?.crew?.clear()
            director?.let { casts?.crew?.add(it) }
        }

    }

    fun getTrailer(): VideoDTO? {
        var trailer: VideoDTO? = null

        videos?.forEach { video ->
            val name = video.name?.toLowerCase()
            if (name != null) {
                if (name.contains("official trailer"))
                    trailer = video
            }
        }

        if (trailer == null) {
            videos?.forEach { video ->
                val name = video.name?.toLowerCase()
                if (name != null) {
                    if (name.contains("trailer"))
                        trailer = video
                }
            }
        }


        if (trailer == null)
            if (!videos.isNullOrEmpty())
                trailer = videos.get(0)

        return trailer
    }
}


