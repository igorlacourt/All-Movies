package com.movies.allmovies

import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("srcIcon")
    fun changeSourceIcon(imageView: ImageView, isInDatabase: Boolean){
        if(isInDatabase) {
            imageView.setImageDrawable(
                ResourcesCompat.getDrawable(imageView.context.resources, R.drawable.ic_check_mark_24dp, null)
            )
        } else {
            imageView.setImageDrawable(
                ResourcesCompat.getDrawable(imageView.context.resources, R.drawable.wish_list_btn_24dp, null)
            )
        }
    }

}