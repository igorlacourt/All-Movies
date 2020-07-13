package com.movies.allmovies.epoxy

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.movies.allmovies.AppConstants
import com.movies.allmovies.R
import com.movies.allmovies.dto.DbMovieDTO
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_list_item.view.*

@EpoxyModelClass(layout = R.layout.movie_list_item)
abstract class MovieModel : EpoxyModelWithHolder<MovieModel.MovieHolder>() {

    @EpoxyAttribute(hash = true)
    lateinit var movieItem: DbMovieDTO

    override fun bind(holder: MovieHolder) {
        super.bind(holder)
        holder.renderMovie(movieItem)

    }

    class MovieHolder : BaseEpoxyHolder() {

        fun renderMovie(movie: DbMovieDTO){

            Picasso.get().load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${movie.poster_path}")
                .placeholder(R.drawable.placeholder)
                .into(itemView.iv_movie_poster)

        }


    }
}