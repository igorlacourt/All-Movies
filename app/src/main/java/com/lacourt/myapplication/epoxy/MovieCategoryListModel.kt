package com.lacourt.myapplication.epoxy

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyHolder
import com.squareup.picasso.Picasso
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.lacourt.myapplication.R


@EpoxyModelClass(layout = R.layout.movie_list_item)
abstract class MovieCategoryListModel : EpoxyModelWithHolder<MovieCategoryListModel.ViewHolder>() {

    @EpoxyAttribute
    var mMoviePoster: String? = null
    @EpoxyAttribute
    var onClickListener: View.OnClickListener? = null

    override fun bind(@NonNull holder: ViewHolder) {
        super.bind(holder)
        val url = "https://image.tmdb.org/t/p/w500"
        Picasso.get().load(url + mMoviePoster!!).into(holder.mPosterImageView)
        holder.mCardView!!.setOnClickListener(onClickListener)
    }

    override fun shouldSaveViewState(): Boolean {
        return true
    }

    class ViewHolder : EpoxyHolder() {

        var mCardView: CardView? = null
        var mPosterImageView: ImageView? = null

        override fun bindView(@NonNull itemView: View) {
            mCardView = itemView.findViewById(R.id.movie_card_view)
            mPosterImageView = itemView.findViewById(R.id.iv_poster)
        }
    }
}