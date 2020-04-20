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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_cast.view.*


class CastsAdapter(
    private val context: Context?,
    private val onCastClick: OnCastClick,
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
            val person = list[position]

            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${person.profilePath}")
                .placeholder(R.drawable.placeholder)
                .into(poster)

            name.text = person.name
            role.text = "as ${person.character}"

            cardView.setOnClickListener {
                val id = list[position].id
                if (id != null) {
                    Log.d("clickgrid", "GridAdapter, setOnClickListener, id = $id")
                    onCastClick.onCastClick(id)
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

    fun addToList(list: List<CastDTO>) {
        list.map { item ->
            this.list.add(item)
            notifyDataSetChanged()
        }

    }
}

class CastsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var poster = itemView.iv_photo
    var cardView = itemView.cv_cast
    var name = itemView.tv_cast_name
    var role = itemView.tv_cast_role
}