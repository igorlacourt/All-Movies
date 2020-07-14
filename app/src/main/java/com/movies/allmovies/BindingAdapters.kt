package com.movies.allmovies

import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("srcIcon")
    fun changeSourceIcon(imageView: ImageView, isInDatabase: Boolean) {
        if (isInDatabase) {
            imageView.setImageDrawable(
                ResourcesCompat.getDrawable(
                    imageView.context.resources,
                    R.drawable.ic_check_mark_24dp,
                    null
                )
            )
        } else {
            imageView.setImageDrawable(
                ResourcesCompat.getDrawable(
                    imageView.context.resources,
                    R.drawable.wish_list_btn_24dp,
                    null
                )
            )
        }
    }

    @JvmStatic
    @BindingAdapter("isVisible")
    fun displayView(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}