package com.lacourt.myapplication.ui.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.dto.Result
import com.squareup.picasso.Picasso


class RecommendedMoviesAdapter(
    private val mContext: Context?,
    private var movies: List<DbMovieDTO>?
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view = convertView

        if (!movies.isNullOrEmpty()) {
            if (convertView == null) {
                val layoutInflater = LayoutInflater.from(mContext)
                view = layoutInflater.inflate(R.layout.movie_list_item, null)
            }
            val poster = view?.findViewById<ImageView>(R.id.iv_poster)
            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${movies!![position].poster_path}")
                .placeholder(R.drawable.placeholder)
                .into(poster)
        }
        return view ?: convertView
    }

    fun setList(newMovies: List<DbMovieDTO>) {
        movies = newMovies
    }

    override fun getCount(): Int {
        return movies?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

}