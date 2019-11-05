package com.lacourt.myapplication.ui

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MoviePosterItemDecorator(val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        outRect.top = offset
        outRect.left = offset / 2
        outRect.right = offset / 2


//        if (layoutParams.spanIndex % 2 == 0) {
//
//            outRect.top = offset
//            outRect.left = offset
//            outRect.right = offset / 2
//
//        } else {
//
//            outRect.top = offset
//            outRect.right = offset
//            outRect.left = offset / 2
//
//        }
    }
}