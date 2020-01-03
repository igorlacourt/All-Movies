package com.movies.allmovies.epoxy

import android.view.View
import androidx.annotation.NonNull
import com.airbnb.epoxy.*
import com.movies.allmovies.R

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