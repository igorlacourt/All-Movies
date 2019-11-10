//package com.lacourt.myapplication.epoxy
//
//import android.view.View
//import android.widget.ImageView
//import androidx.annotation.NonNull
//import androidx.cardview.widget.CardView
//import com.airbnb.epoxy.*
//import com.squareup.picasso.Picasso
//import com.lacourt.myapplication.AppConstants
//import com.airbnb.epoxy.CallbackProp
//import androidx.annotation.Nullable
//import com.airbnb.epoxy.EpoxyAttribute
//import com.lacourt.myapplication.R
//import com.lacourt.myapplication.ui.OnItemClick
//import kotlinx.android.synthetic.main.movie_list_item.view.*
//
//
//@EpoxyModelClass(layout = R.layout.movie_list_item)
//abstract class MovieModelWithRender(val onItemClick: OnItemClick) : EpoxyModelWithHolder<MovieListModel.ViewHolder>() {
//
//    @EpoxyAttribute
//    var mMoviePoster: String? = null
//
//    @EpoxyAttribute(hash=false)
//    var clickListener: (Int) -> Unit = {}
//
////    @EpoxyAttribute(hash = false)
////    lateinit var clickListener: (Int) -> Unit
//
//    override fun bind(@NonNull holder: ViewHolder) {
//        super.bind(holder)
//        holder.renderView(mMoviePoster)
//        holder.setUpListener(clickListener)
//
//    }
//
//    override fun shouldSaveViewState(): Boolean {
//        return true
//    }
//
//    class ViewHolder : BaseEpoxyHolder() {
//        fun setUpListener(clickListener: (Int) -> Unit){
//            clickListener.invoke(290859)
//        }
//
//        fun renderView(mMoviePoster: String?){
//            val url = "https://image.tmdb.org/t/p/w500"
//
//            val imagePath = mMoviePoster ?: ""
//            Picasso.get().load(AppConstants.TMDB_IMAGE_BASE_URL_W185 + imagePath)
//                .placeholder(R.drawable.placeholder)
//                .into(itemView.iv_poster)
//        }
//    }
//
//
//
//}