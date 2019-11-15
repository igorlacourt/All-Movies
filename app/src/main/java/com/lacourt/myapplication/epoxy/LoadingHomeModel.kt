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

@EpoxyModelClass(layout = R.layout.layout_loading_home)
abstract class LoadingHomeModel : EpoxyModelWithHolder<LoadingHomeModel.ViewHolder>() {
    override fun bind(@NonNull holder: ViewHolder) {
        super.bind(holder)
    }

    override fun shouldSaveViewState(): Boolean {
        return true
    }

    class ViewHolder : EpoxyHolder() {
        override fun bindView(itemView: View) {
        }
    }

}