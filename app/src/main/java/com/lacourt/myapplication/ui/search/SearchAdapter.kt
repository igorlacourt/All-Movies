package com.lacourt.myapplication.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.model.dto.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.searched_list_item.view.*

class SearchAdapter(
    private val onSearchedItemClick: OnSearchedItemClick,
    private var list: ArrayList<Movie>
) : RecyclerView.Adapter<SearchViewHolder>() {

    private val context = onSearchedItemClick as Context

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
                .placeholder(R.drawable.clapperboard)
                .into(backdrop)

            itemLayout.setOnClickListener {
                onSearchedItemClick.onSearchItemClick(list[position].id)
            }
        }

    }

    fun setList(list: ArrayList<Movie>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var backdrop = itemView.searched_backdrop
    var title = itemView.searched_title
    var itemLayout = itemView.search_item_layout
}

interface OnSearchedItemClick {
    fun onSearchItemClick(int: Int)
}