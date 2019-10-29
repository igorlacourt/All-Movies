package com.lacourt.myapplication.epoxy

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.dto.GenreXDTO
import com.lacourt.myapplication.ui.home.GenresAdapter
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.top_trending_movie)
abstract class TopTrendingMovieModel(val context: Context?) : EpoxyModelWithHolder<TopTrendingMovieModel.ViewHolder>() {

    @EpoxyAttribute
    lateinit var genres: ArrayList<GenreXDTO>

    @EpoxyAttribute
    var backdropPath: String? = null

    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: ViewHolder) {
        super.bind(holder)
        Picasso.get().load(AppConstants.TMDB_IMAGE_BASE_URL_W500 + backdropPath)
            .placeholder(R.drawable.clapperboard)
            .into(holder.image)
        holder.constraintLayout?.setOnClickListener(clickListener)
        holder.recyclerview?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        holder.recyclerview?.adapter = GenresAdapter(context, genres)
    }

    class ViewHolder : EpoxyHolder() {
        var image: ImageView? = null
        var constraintLayout: ConstraintLayout? = null
        var recyclerview: RecyclerView? = null
        override fun bindView(itemView: View) {
            image = itemView.findViewById(R.id.iv_top_trending_movie)
            constraintLayout = itemView.findViewById(R.id.ly_top_trending_movie)
            recyclerview = itemView.findViewById(R.id.genres_list)
        }
    }

}