package com.lacourt.myapplication.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.ui.OnItemClick
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieAdapter(
    private val context: Context?,
    private val onItemClick: OnItemClick,
    private var movies: List<DbMovieDTO>
) : PagedListAdapter<DbMovieDTO, MovieViewHolder>(DIFF_CALLBACK) {

    fun setMovies(newMovies: List<DbMovieDTO>?) {
        newMovies?.let {
            movies = it
        }
        notifyDataSetChanged()
    }

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
        val movieDTO: DbMovieDTO? = getItem(position)

        holder.apply {
            Picasso.get().load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${movieDTO!!.poster_path}")
                .placeholder(R.drawable.placeholder)
                .into(poster)

            cardView.setOnClickListener {
                Log.d("clickgrid", "MovieAdapter, setOnClickListener, id = ${movies[position].id}")
                onItemClick.onItemClick(movieDTO.id)
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<DbMovieDTO>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldMovieDTO: DbMovieDTO,
                newMovieDTO: DbMovieDTO
            ) = oldMovieDTO.id == newMovieDTO.id

            override fun areContentsTheSame(
                oldItem: DbMovieDTO,
                newItem: DbMovieDTO
            ) = oldItem == newItem
        }
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var poster = itemView.iv_poster
    var cardView = itemView.movie_card_view
    var layout = itemView.movie_layout

}