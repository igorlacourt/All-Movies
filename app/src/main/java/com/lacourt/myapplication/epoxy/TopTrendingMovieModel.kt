package com.lacourt.myapplication.epoxy

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.top_trending_movie)
abstract class TopTrendingMovieModel : EpoxyModelWithHolder<TopTrendingMovieModel.ViewHolder>() {

    @EpoxyAttribute
    var backdropPath: String? = null

    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: ViewHolder) {
        super.bind(holder)
        Picasso.get().load(AppConstants.TMDB_IMAGE_BASE_URL_W500 + backdropPath)
            .placeholder(R.drawable.clapperboard)
            .into(holder.image)
        holder.framaLayout?.setOnClickListener(clickListener)
    }

    class ViewHolder: EpoxyHolder() {
        var image: ImageView? = null
        var framaLayout: ConstraintLayout? = null
        override fun bindView(itemView: View) {
            image = itemView.findViewById(R.id.iv_top_trending_movie)
            framaLayout = itemView.findViewById(R.id.ly_top_trending_movie)
        }
    }

}