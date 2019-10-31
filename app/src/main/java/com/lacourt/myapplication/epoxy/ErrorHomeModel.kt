package com.lacourt.myapplication.epoxy

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.lacourt.myapplication.R

@EpoxyModelClass(layout = R.layout.home_error_layout)
abstract class ErrorHomeModel : EpoxyModelWithHolder<ErrorHomeModel.HomeErrorHolder>(){
    @EpoxyAttribute
    var message: String = ""

    override fun bind(holder: HomeErrorHolder) {
        super.bind(holder)
        holder.message?.text = message
    }

    class HomeErrorHolder: EpoxyHolder() {
        var message: TextView? = null

        override fun bindView(itemView: View) {
            message = itemView.findViewById(R.id.tv_home_error)
        }

    }
}