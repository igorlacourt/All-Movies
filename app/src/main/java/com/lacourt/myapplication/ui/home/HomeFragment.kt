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
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.lacourt.myapplication.R
import com.lacourt.myapplication.dto.GenreXDTO
import com.lacourt.myapplication.epoxy.*
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.ui.OnItemClick
import com.lacourt.myapplication.viewmodel.HomeViewModel


class HomeFragment : Fragment(), OnItemClick {
    private lateinit var homeViewModel: HomeViewModel
    //    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView: EpoxyRecyclerView

    private val movieController by lazy { MovieController(context, this) }

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
//        val adapter = MovieAdapter(context, onItemClick)

        Log.d("clicklog", "before initialize movieController")
        Log.d("genreslog", "HomeFragment, onCreateView() called")

        val adapter = movieController.adapter

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter

        homeViewModel.listsOfMovies?.observe(this, Observer { response ->
            when (response?.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        movieController.submitListsOfMovies(it, null)
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

        homeViewModel.topTrendingMovie?.observe(this, Observer { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        if (it.genres != null)
//                            genresAdapter.setList(it.genres)
                        movieController.submitTopTrendingMovie(it)
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

        homeViewModel.trendingMovies?.observe(this, Observer { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        movieController.submitTrendingMovies(it, null)
                        recyclerView.setController(movieController)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                    Log.d("errorBoolean", "HomeFragment, Resource.Status.ERROR")
                    movieController.submitTrendingMovies(null, response.error)
                    Toast.makeText(context, "${response.error?.message}, ${response.error?.cd}", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.INVISIBLE
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

    override fun onItemClick(id: Int) {
        val homeToDetailsFragment =
            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(id)
        findNavController().navigate(homeToDetailsFragment)
    }

}
//        (recyclerView.adapter as MovieAdapter).registerAdapterDataObserver(object :
//            RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
//                recyclerView.scrollToPosition(0)
//            }
//        })