package com.lacourt.myapplication.epoxy

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
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.dto.GenreXDTO
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.top_trending_movie)
abstract class TopTrendingMovieModel(val context: Context?) :
    EpoxyModelWithHolder<TopTrendingMovieModel.ViewHolder>() {

    @EpoxyAttribute
    var backdropPath: String? = null

    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    @EpoxyAttribute
    lateinit var genresList: List<GenreXDTO>

    @EpoxyAttribute
    lateinit var title: String

    override fun bind(holder: ViewHolder) {
        super.bind(holder)
        Picasso.get().load(AppConstants.TMDB_IMAGE_BASE_URL_ORIGINAL + backdropPath)
            .placeholder(R.drawable.clapperboard)
            .into(holder.image)
        holder.framaLayout?.setOnClickListener(clickListener)
        holder.title?.text = title
        holder.genres?.text = ""

        Log.d("genreslog", "TopTrendingMovieModel, bind(called)")

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
                        "Action" -> color =
                            context?.resources?.getColor(R.color.genreActionColor)
                        "Western" -> color =
                            context?.resources?.getColor(R.color.genreWesternColor)
                        "Thriller" -> color =
                            context?.resources?.getColor(R.color.genreThrillerColor)
                        "War" -> color = context?.resources?.getColor(R.color.genreWarColor)
                        "Horror" -> color =
                            context?.resources?.getColor(R.color.genreHorrorColor)
                        "Terror" -> color =
                            context?.resources?.getColor(R.color.genreTerrorColor)
                        "Documentary" -> color =
                            context?.resources?.getColor(R.color.genreDocumentaryColor)
                    }
                }

                if (color != null)
                    s[0..5] = ForegroundColorSpan(color!!)
                else
                    s[0..5] = ForegroundColorSpan(Color.GRAY)

                holder.genres?.append(s)
            }
        }
    }

    class ViewHolder : EpoxyHolder() {
        var image: ImageView? = null
        var framaLayout: ConstraintLayout? = null
        var title: TextView? = null
        var genres: TextView? = null
        //        var recyclerView: RecyclerView? = null
        override fun bindView(itemView: View) {
            image = itemView.findViewById(R.id.iv_top_trending_movie)
            framaLayout = itemView.findViewById(R.id.ly_top_trending_movie)
            title = itemView.findViewById(R.id.tv_top_trending_movie_title)
            genres = itemView.findViewById(R.id.tv_genres)
//            recyclerView = itemView.findViewById(R.id.rv_genres)
        }
    }

}