package com.lacourt.myapplication.epoxy

import android.view.View
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.*
import com.squareup.picasso.Picasso
import com.lacourt.myapplication.AppConstants
import com.airbnb.epoxy.EpoxyAttribute
import com.lacourt.myapplication.R

@EpoxyModelClass(layout = R.layout.movie_list_item)
abstract class MovieListModel : EpoxyModelWithHolder<MovieListModel.ViewHolder>() {

    @EpoxyAttribute
    var mMoviePoster: String? = null

    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    override fun bind(@NonNull holder: ViewHolder) {
        super.bind(holder)
        val imagePath = mMoviePoster ?: ""
        Picasso.get().load(AppConstants.TMDB_IMAGE_BASE_URL_W185 + imagePath)
            .placeholder(R.drawable.placeholder)
            .into(holder.poster)
        holder.cardView?.setOnClickListener(clickListener)
    }

    override fun shouldSaveViewState(): Boolean {
        return true
    }

    class ViewHolder : EpoxyHolder() {
        var poster: ImageView? = null
        var cardView: CardView? = null
        override fun bindView(itemView: View) {
            poster = itemView.findViewById(R.id.iv_poster)
            cardView = itemView.findViewById(R.id.movie_card_view)
        }
    }

}