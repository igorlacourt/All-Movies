package com.lacourt.myapplication.ui.mylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.domainmodel.MyListItem
import com.lacourt.myapplication.ui.OnItemClick
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MyListAdapter(
    private val context: Context?,
    private val onItemClick: OnItemClick,
    private var list: ArrayList<MyListItem>
) : RecyclerView.Adapter<MyListHolder>() {

//    private val context = onMyListItemClick as Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false)
        return MyListHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyListHolder, position: Int) {

        holder.apply {
            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${list[position].poster_path}")
                .placeholder(R.drawable.clapperboard)
                .into(poster)

            cardView.setOnClickListener {
                val id = list[position].id
                if(id != null)
                    onItemClick.onItemClick(id)
                else
                    Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun setList(list: List<MyListItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}

class MyListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var poster = itemView.iv_poster
    var cardView = itemView.movie_card_view
}

