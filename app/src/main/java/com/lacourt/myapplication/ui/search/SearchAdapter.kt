package com.lacourt.myapplication.ui.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.dto.MovieDTO
import com.lacourt.myapplication.ui.OnItemClick
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.searched_list_item.view.*

class SearchAdapter(private val context: Context?,
                    private val onItemClick: OnItemClick,
                    private var list: ArrayList<MovieDTO>
) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.searched_list_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.apply {
            title.text = list[position].title
            Picasso.get().load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${list[position].backdrop_path}")
                .placeholder(R.drawable.placeholder)
                .into(backdrop)

            itemLayout.setOnClickListener {
                Log.d("clickgrid", "SearchAdapter, setOnClickListener, id = ${list[position].id}")
                onItemClick.onItemClick(list[position].id)
            }
        }

    }

    fun setList(list: ArrayList<MovieDTO>?) {
        Log.d("searchlog", "setList called")
        this.list.clear()
        list?.let {
            this.list.addAll(it)
        }
        notifyDataSetChanged()
    }
}

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var backdrop = itemView.searched_backdrop
    var title = itemView.searched_title
    var itemLayout = itemView.search_item_layout
}

