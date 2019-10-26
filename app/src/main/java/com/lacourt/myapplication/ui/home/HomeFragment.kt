package com.lacourt.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.lacourt.myapplication.R
import com.lacourt.myapplication.epoxy.*
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.ui.OnMovieClick
import com.lacourt.myapplication.viewmodel.HomeViewModel


class HomeFragment : Fragment(), OnMovieClick {
    private lateinit var homeViewModel: HomeViewModel
    //    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView: EpoxyRecyclerView

    val movieController by lazy { MovieController(this) }

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
//        val adapter = MovieAdapter(context, onMovieClick)

        Log.d("clicklog", "before initialize movieController")
        val adapter = movieController.adapter

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter

        homeViewModel.trendingAll?.observe(this, Observer { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        movieController.submitAllTrending(it)
                        recyclerView.setController(movieController)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                }
            }
        })
//        homeViewModel.latestTv?.observe(this, Observer { response ->
//            when (response.status) {
//                Resource.Status.SUCCESS -> {
//                    response.data?.let {
//                        movieController.submitLatestTv(it)
//                        recyclerView.setController(movieController)
//                    }
//                    progressBar.visibility = View.INVISIBLE
//                }
//                Resource.Status.LOADING -> {
//                }
//                Resource.Status.ERROR -> {
//                }
//            }
//        })
        homeViewModel.trendingTv?.observe(this, Observer { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        movieController.submitTrendingTv(it)
                        recyclerView.setController(movieController)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                }
            }
        })
        homeViewModel.topRatedMovies?.observe(this, Observer { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        movieController.submitTopRatedMovies(it)
                        recyclerView.setController(movieController)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                }
            }
        })
        homeViewModel.topRatedTv?.observe(this, Observer { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        movieController.submitTopRatedTv(it)
                        recyclerView.setController(movieController)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                }
            }
        })

        homeViewModel.upcomingMovies?.observe(this, Observer { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        movieController.submitUpcomingMovies(it)
                        recyclerView.setController(movieController)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                }
            }
        })

        homeViewModel.popularMovies?.observe(this, Observer { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        movieController.submitPopularMovies(it)
                        recyclerView.setController(movieController)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                }
            }
        })

        return root
    }

    override fun onMovieClick(id: Int) {
        if (id != 0) {
            val homeToDetailsFragment =
                HomeFragmentDirections.actionNavigationHomeToDetailsFragment(id)
            findNavController().navigate(homeToDetailsFragment)
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
        }
    }

}
//        (recyclerView.adapter as MovieAdapter).registerAdapterDataObserver(object :
//            RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
//                recyclerView.scrollToPosition(0)
//            }
//        })