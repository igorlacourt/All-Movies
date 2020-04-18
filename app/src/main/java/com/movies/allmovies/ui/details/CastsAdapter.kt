package com.movies.allmovies.ui.details

import android.app.Activity
import com.movies.allmovies.dto.CastDTO
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.movies.allmovies.AppConstants
import com.movies.allmovies.R
import com.movies.allmovies.ui.OnItemClick
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_cast.view.*
import kotlinx.android.synthetic.main.movie_list_item.view.*


class CastsAdapter(
    private val context: Context?,
    private val onItemClick: OnItemClick,
    private var list: ArrayList<CastDTO>
) : RecyclerView.Adapter<CastsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastsHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.list_item_cast, parent, false)
        return CastsHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CastsHolder, position: Int) {
        Log.d("grid-log", "onBindViewHolder() called, position = $position, list.size = ${list.size}")
        // get device dimensions
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

        holder.apply {
            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${list[position].profilePath}")
                .placeholder(R.drawable.placeholder)
                .into(poster)

            cardView.setOnClickListener {
                val id = list[position].id
                if (id != null) {
                    Log.d("clickgrid", "GridAdapter, setOnClickListener, id = $id")
                    onItemClick.onItemClick(id)
                }
                else {
                    Toast.makeText(
                        context,
                        "Sorry. Can not load this movie. :/",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    fun setList(list: List<CastDTO>) {
        Log.d("grid-log", "setList() called")
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}

class CastsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var poster = itemView.iv_photo
    var cardView = itemView.cv_cast
}