package com.movies.allmovies.epoxy

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.movies.allmovies.R

@EpoxyModelClass(layout = R.layout.carousel_header)
abstract class HeaderModel : EpoxyModelWithHolder<HeaderModel.ViewHolder>() {

    @EpoxyAttribute
    var header: String? = null

    override fun bind(holder: ViewHolder) {
        super.bind(holder)
        holder.renderHeader(header)
    }

    class ViewHolder: BaseEpoxyHolder() {
        fun renderHeader(header: String?){
            itemView.findViewById<TextView>(R.id.tv_header).text = header
        }
    }



}