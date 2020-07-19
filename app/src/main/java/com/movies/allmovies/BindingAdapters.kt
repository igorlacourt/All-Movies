package com.movies.allmovies

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import kotlin.math.floor

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

    @JvmStatic
    @BindingAdapter("voteAverageColor")
    fun voteAverageColor(tv: TextView, avg: Double?) {
        if (avg != null) {
            var color = R.color.avg0until4
            val vote: Int = floor(avg).toInt()
            when (vote) {
                10 -> color = R.color.avg8until10
                9 -> color = R.color.avg8until10
                8 -> color = R.color.avg8until10
                7 -> color = R.color.avg6until8
                6 -> color = R.color.avg6until8
                5 -> color = R.color.avg4until6
                4 -> color = R.color.avg4until6
                3 -> color = R.color.avg0until4
                2 -> color = R.color.avg0until4
                1 -> color = R.color.avg0until4
            }
            tv.text = "${avg.toString()} vote average"
            tv.context?.let { tv.setTextColor(ContextCompat.getColor(it, color)) }
        }
    }

}