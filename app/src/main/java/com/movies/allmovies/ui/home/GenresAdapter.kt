package com.movies.allmovies.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movies.allmovies.R
import kotlinx.android.synthetic.main.genre_list_item.view.*

class GenresAdapter(val context: Context?, val genres: ArrayList<String>) :
    RecyclerView.Adapter<GenresAdapter.GenresHolder>() {

    var genreNames: ArrayList<String>? = null

//    init {
//        setList(genres)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.genre_list_item, parent, false)
        return GenresHolder(view)
    }

    override fun getItemCount(): Int {
//      return  if (genreNames != null) genreNames!!.size else 0
        genreNames?.let { return it.size } ?: return 0
    }

    override fun onBindViewHolder(holder: GenresHolder, position: Int) {
        holder.name.text = genres[position]
//        if (genreNames != null)
//            holder.name.text = genreNames!![position]
//        genreNames?.forEach {
//            if (it == "•") holder.name.setTextColor(Color.MAGENTA)
//        }
    }

//    fun setList(newList: List<GenreXDTO>) {
//        for (i in 0 until newList.size) {
//
//            val name = newList[i].name
//
//            genreNames?.add(name)
//            if (i == newList.size - 1) {
//                genreNames?.add(name)
//            } else {
//                genreNames?.add(name)
//                if (i % 2 != 0) {
//                    genreNames?.add("•")
//                }
//            }
//        }
//        notifyDataSetChanged()
//    }

    class GenresHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.tv_genre_name
    }
}