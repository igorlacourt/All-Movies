package com.movies.allmovies.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.airbnb.epoxy.*
import com.movies.allmovies.R
import com.squareup.picasso.Picasso

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class CarouselItemCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var poster: ImageView? = null

    init {
        inflate(context, R.layout.movie_list_item, this)
        orientation = VERTICAL
        poster = (findViewById(R.id.iv_poster))

        val url = "https://image.tmdb.org/t/p/w500"
        Picasso.get().load(url + "/jpfkzbIXgKZqCZAkEkFH2VYF63s.jpg").into(poster)

    }

    companion object {
        private const val TAG = "CarouselItemCustomView"
    }
}
