package com.movies.allmovies.epoxy

import android.view.View
import androidx.annotation.CallSuper
import com.airbnb.epoxy.EpoxyHolder

abstract class BaseEpoxyHolder: EpoxyHolder() {
    lateinit var itemView: View

    @CallSuper
    override fun bindView(itemView: View) {
        this.itemView = itemView
    }
}