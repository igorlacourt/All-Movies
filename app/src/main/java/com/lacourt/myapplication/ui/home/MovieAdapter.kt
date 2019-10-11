package com.lacourt.myapplication.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.model.dbmodel.DbMovie
import com.lacourt.myapplication.ui.OnMovieClick
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieAdapter(
    private val context: Context?,
    private val onMovieClick: OnMovieClick
) : PagedListAdapter<DbMovie, MovieViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.movie_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: DbMovie? = getItem(position)

        holder.apply {
            Picasso.get().load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${movie!!.poster_path}")
                .placeholder(R.drawable.clapperboard)
                .into(poster)

            cardView.setOnClickListener {
                onMovieClick.onMovieClick(movie.id)
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<DbMovie>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldMovie: DbMovie,
                newMovie: DbMovie
            ) = oldMovie.id == newMovie.id

            override fun areContentsTheSame(
                oldItem: DbMovie,
                newItem: DbMovie
            ) = oldItem == newItem
        }
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var poster = itemView.iv_poster
    var cardView = itemView.movie_card_view
    var layout = itemView.movie_layout

}