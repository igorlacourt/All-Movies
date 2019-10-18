package com.lacourt.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.ui.OnMovieClick
import com.lacourt.myapplication.ui.details.DetailsActivity
import com.lacourt.myapplication.viewmodel.HomeViewModel


class HomeFragment : Fragment(), OnMovieClick {

    private val onMovieClick = this as OnMovieClick
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("callstest", "onCreateView called\n")
        val root = inflater.inflate(R.layout.fragment_home, container, false)

//        var shimmerContainer = container?.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
//        shimmerContainer?.visibility = View.VISIBLE
//        shimmerContainer?.startShimmer()

        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val progressBar: ProgressBar = root.findViewById(R.id.progress_circular)
        progressBar.visibility = View.VISIBLE
        recyclerView = root.findViewById(R.id.movie_list)
        val adapter = MovieAdapter(context, onMovieClick)

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter

        (recyclerView.adapter as MovieAdapter).registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })

        homeViewModel.movies?.observe(this, Observer { pagedList ->
            if(pagedList != null && pagedList.isNotEmpty()) {
                adapter.submitList(pagedList)
                progressBar.visibility = View.INVISIBLE
            }
        })

        return root
    }

    override fun onMovieClick(id: Int) {
        val i = Intent(context, DetailsActivity::class.java)
        i.putExtra("id", id)
        context?.startActivity(i)
    }

}
