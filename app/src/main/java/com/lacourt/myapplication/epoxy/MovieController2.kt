//package com.lacourt.myapplication.epoxy
//
//import androidx.core.content.ContextCompat.startActivity
//import android.content.Intent
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import com.airbnb.epoxy.OnModelClickListener
//import android.R.id
//import android.content.Context
//import android.view.View
//import com.airbnb.epoxy.CarouselModel_
//import com.lacourt.myapplication.dto.DbMovieDTO
//import com.airbnb.epoxy.TypedEpoxyController as TypedEpoxyController1
//
//
//class MovieController2 : TypedEpoxyController1<ArrayList<DbMovieDTO>> {
//
//    private val mContext: Context
//
//    constructor(context: Context) {
//        mContext = context
//    }
//
//    override fun buildModels(data: ArrayList<DbMovieDTO>) {
//
//        for (i in 0..9) {
//            CarouselModel_().id(i.toLong())
//                .models(buildMovieList(i.toLong(), data)).addTo(this)
//        }
//    }
//
//    /* This creates a MovieCetegoryListModel for each movie in the nested RecyclerView, this model
//     * represents the view of each movie.
//     */
//    private fun buildMovieList(
//        id: Long,
//        movies: ArrayList<DbMovieDTO>
//    ): ArrayList<MovieCategoryListModel_> {
//
//        val movieCategory = ArrayList<MovieCategoryListModel_>()
//        for (movie in movies) {
//            movieCategory.add(
//                MovieCategoryListModel_()
//                    .id(movie.getmId(), id)
//                    .mMoviePoster(movie.getPosterPath())
//                    .mMovieTitle(movie.getTitle())
//                    .onClickListener(object :
//                        OnModelClickListener<MovieCategoryListModel_, MovieCategoryListModel.ViewHolder> {
//                        override fun onClick(
//                            model: MovieCategoryListModel_,
//                            parentView: MovieCategoryListModel.ViewHolder,
//                            clickedView: View,
//                            position: Int
//                        ) {
//
//                        }
//                    })
//            )
//        }
//
//        return movieCategory
//    }
//}