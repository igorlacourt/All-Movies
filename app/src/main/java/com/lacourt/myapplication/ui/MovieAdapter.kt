package com.lacourt.myapplication.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.R
import com.lacourt.myapplication.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieAdapter(context: Context?, onMovieClick: OnMovieClick) : PagedListAdapter<Movie, MovieViewHolder>(DIFF_CALLBACK) {
    val context = context
    val onMovieClick = onMovieClick

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
        val movie: Movie? = getItem(position)

        holder.title.text = movie?.title
        holder.rate.text = movie?.vote_average.toString()

        if(movie?.genres == null) {
            Log.d("rxlog", "MovieAdapter: movie.genres is null")
            holder.genreAndDate.text = " ... , ${movie?.release_date?.subSequence(0, 4)}"
        }
        else {
            Log.d("rxlog", "MovieAdapter: movie.genres NOT null")
            holder.genreAndDate.text =
                "${movie?.genres?.get(0)}, ${movie?.release_date?.subSequence(0, 4)}"
        }

//        holder.poster.setImageBitmap(decodeImage(movie.encoded_poster))
        Picasso.get().load("https://image.tmdb.org/t/p/w185/${movie!!.poster_path}")
            .placeholder(R.drawable.clapperboard)
            .into(holder.poster)

        holder.cardView.setOnClickListener {
            onMovieClick.onMovieClick(movie)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Movie>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldMovie: Movie,
                newMovie: Movie
            ) = oldMovie.id == newMovie.id

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ) = oldItem == newItem
        }
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // We'll use this field to showcase matching the holder from the test.
    var IsInTheMiddle: Boolean = false
    var poster = itemView.iv_poster
    var title = itemView.title
    var rate = itemView.rating
    var genreAndDate = itemView.genre_date
    var cardView = itemView.movie_card_view
    var layout = itemView.movie_layout

}