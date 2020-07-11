package com.movies.allmovies.epoxy

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.set
import androidx.core.text.toSpannable
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.movies.allmovies.AppConstants
import com.movies.allmovies.R
import com.movies.allmovies.dto.GenreXDTO
import com.squareup.picasso.Picasso
import androidx.core.content.res.ResourcesCompat


@EpoxyModelClass(layout = R.layout.top_trending_movie)
abstract class TopTrendingMovieModel(val context: Context?, val isInDatabase: Boolean) :
    EpoxyModelWithHolder<TopTrendingMovieModel.ViewHolder>() {

    @EpoxyAttribute
    var backdropPath: String? = null

    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    @EpoxyAttribute
    lateinit var trailerClickListener: View.OnClickListener

    @EpoxyAttribute
    lateinit var myListClickListener: View.OnClickListener

    @EpoxyAttribute
    lateinit var genresList: List<GenreXDTO>

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    lateinit var myListButton: ConstraintLayout

    @EpoxyAttribute
    var myListIcon: ImageView? = null

    override fun bind(holder: ViewHolder) {
        super.bind(holder)
        Picasso.get().load(AppConstants.TMDB_IMAGE_BASE_URL_ORIGINAL + backdropPath)
            .placeholder(R.drawable.placeholder)
            .into(holder.image)
        holder.title?.text = title
        holder.genres?.text = ""

        Log.d("genreslog", "TopTrendingMovieModel, bind(called)")

        drawGenres(holder)
        drawMyListIcon(holder)
        setListeners(holder)

    }

    fun drawMyListIcon(holder: ViewHolder) {
        if (context != null) {
            if (isInDatabase)
                holder.myListIcon?.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_check_mark_24dp,
                        null
                    )
                )
            else
                holder.myListIcon?.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.wish_list_btn_24dp,
                        null
                    )
                )
        }
    }

    fun drawGenres(holder: ViewHolder){
        for (i in 0 until genresList.size) {
            Log.d("genreslog", "TopTrendingMovieModel, for called, index = $i")
            holder.genres?.append(genresList[i].name)
            if (i < genresList.size - 1) {
                Log.d("genreslog", "TopTrendingMovieModel, if is true, index = $i")
                val s = "  •  ".toSpannable()

                var color = context?.resources?.getColor(R.color.genreFamilyColor)
                genresList.map {
                    when (it.name) {
                        "Drama" -> color = context?.resources?.getColor(R.color.genreDramaColor)
                        "Action" -> color = context?.resources?.getColor(R.color.genreActionColor)
                        "Western" -> color = context?.resources?.getColor(R.color.genreWesternColor)
                        "Thriller" -> color =
                            context?.resources?.getColor(R.color.genreThrillerColor)
                        "War" -> color = context?.resources?.getColor(R.color.genreWarColor)
                        "Horror" -> color = context?.resources?.getColor(R.color.genreHorrorColor)
                        "Terror" -> color = context?.resources?.getColor(R.color.genreTerrorColor)
                        "Documentary" -> color =
                            context?.resources?.getColor(R.color.genreDocumentaryColor)
                    }
                }

                s[0..5] = color?.let { ForegroundColorSpan(it) } ?: ForegroundColorSpan(Color.GRAY)

                holder.genres?.append(s)
            }

        }
    }

    fun setListeners(holder: ViewHolder){
        holder.trailerButton?.setOnClickListener(trailerClickListener)
        holder.myListButton?.setOnClickListener(myListClickListener)
        holder.learnMoreButton?.setOnClickListener(clickListener)
        holder.frameLayout?.setOnClickListener(clickListener)
    }

    class ViewHolder : EpoxyHolder() {
        var image: ImageView? = null
        var frameLayout: ConstraintLayout? = null
        var title: TextView? = null
        var genres: TextView? = null
        var trailerButton: ConstraintLayout? = null
        var myListButton: ConstraintLayout? = null
        var myListIcon: ImageView? = null
        var learnMoreButton: ConstraintLayout? = null

        override fun bindView(itemView: View) {
            image = itemView.findViewById(R.id.iv_top_trending_movie)
            frameLayout = itemView.findViewById(R.id.ly_top_trending_movie)
            title = itemView.findViewById(R.id.tv_top_trending_movie_title)
            genres = itemView.findViewById(R.id.tv_genres)
            trailerButton = itemView.findViewById(R.id.btn_top_trending_trailer)
            myListButton = itemView.findViewById(R.id.i_btn_add_to_my_list)
            myListIcon = itemView.findViewById(R.id.bt_add_to_list)
            learnMoreButton = itemView.findViewById(R.id.btn_learn_more)
        }
    }

}