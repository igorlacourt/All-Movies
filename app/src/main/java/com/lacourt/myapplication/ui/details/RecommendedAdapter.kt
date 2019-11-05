package com.lacourt.myapplication.ui.details

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.ui.OnItemClick
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.movie_list_item.view.*
import java.lang.Exception

class RecommendedAdapter(
    private val context: Context?,
    private val onItemClick: OnItemClick,
    private var list: ArrayList<DbMovieDTO>
) : RecyclerView.Adapter<RecommendedHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.list_item_recommended, parent, false)
        return RecommendedHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecommendedHolder, position: Int) {
//        var width: Int? = null
//        var height: Int? = null
//        val xdpi = context?.resources?.displayMetrics?.xdpi
//        val configuration = context?.resources?.configuration?.screenWidthDp
//
//        if (xdpi != null) {
//            width = xdpi.div(3).toInt()
//        }
//        width?.let { height = (it * 1.5).toInt() }
//
//        width?.let {
//            holder.poster.maxWidth = it
//            holder.poster.minimumWidth = it
//        }
//        height?.let {
//            holder.poster.maxHeight = it
//            holder.poster.minimumHeight = it
//        }

        holder.poster.minimumHeight = holder.poster.minimumWidth * 2

        holder.apply {
            val picasso = Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${list[position].poster_path}")
                .placeholder(R.drawable.clapperboard)
                .into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        bitmap?.let { poster.minimumHeight = bitmap.width * 2 }
                        poster.setImageBitmap(bitmap)
                    }
                })

//            if (width != null && height != null){
//                picasso
//                    .resize(width*2, height!!*2)
//                    .centerCrop()
//                    .into(poster)
//            }

            cardView.setOnClickListener {
                val id = list[position].id
                if (id != null)
                    onItemClick.onItemClick(id)
                else
                    Toast.makeText(
                        context,
                        "Sorry. Can not load this movie. :/",
                        Toast.LENGTH_SHORT
                    ).show()

            }
        }

    }

    fun setList(list: List<DbMovieDTO>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}

class RecommendedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var poster = itemView.iv_poster
    var cardView = itemView.movie_card_view
}

