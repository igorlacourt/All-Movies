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
import com.lacourt.myapplication.dto.DbMovieDTO
import com.lacourt.myapplication.dto.GenreXDTO
import com.lacourt.myapplication.epoxy.*
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.ui.OnItemClick
import com.lacourt.myapplication.viewmodel.HomeViewModel

class HomeFragment : Fragment(), OnItemClick, OnRecreate {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: EpoxyRecyclerView

    private val recreate = this as OnRecreate
    private val itemClick = this as OnItemClick

    private val movieController by lazy { MovieController(context, itemClick, recreate) }

    var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("callstest", "onCreateView called\n")
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        Log.d("refreshLog", "onCreateView() called")

        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)

        progressBar = root.findViewById(R.id.progress_circular)
        progressBar?.visibility = View.VISIBLE
        recyclerView = root.findViewById(R.id.movie_list)

        Log.d("clicklog", "before initialize movieController")
        Log.d("genreslog", "HomeFragment, onCreateView() called")

        val adapter = movieController.adapter

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter

        homeViewModel.listsOfMovies?.observe(this, Observer { response ->
            when (response?.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("listsLog", "HomeFragment, response size = ${response?.data?.size}")
                    response.data?.let {
                        Log.d("refresh", "HomeFragment, listsOfMovies?.observe, success response = ${response.data.size}")
                        movieController.submitListsOfMovies(it, null)
                        recyclerView.setController(movieController)
                    }
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                    Log.d("refresh", "HomeFragment, listsOfMovies?.observe, fail, response = ${response.error?.cd}")
                    movieController.submitListsOfMovies(null, response.error)
                    Toast.makeText(context, "${response.error?.message}, ${response.error?.cd}", Toast.LENGTH_LONG).show()
                }
            }
        })

        homeViewModel.topTrendingMovie?.observe(this, Observer { details ->
            when (details.status) {
                Resource.Status.SUCCESS -> {
                    details.data?.let {
                        if (it.genres != null)
                            Log.d("refresh", "HomeFragment, topTrendingMovie?.observe, success, response = ${it.title}")
                        movieController.submitTopTrendingMovie(it, null)
                        recyclerView.setController(movieController)
                        progressBar?.visibility = View.INVISIBLE
                    }
                    progressBar?.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                    progressBar?.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    Log.d("refresh", "HomeFragment, topTrendingMovie?.observe, fail, response = ${details.error?.cd}")
                    movieController.submitTopTrendingMovie(null, details.error)
                    progressBar?.visibility = View.INVISIBLE
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


    override fun refresh() {
        Log.d("refresh", "HomeFragment, refresh() called")

        homeViewModel.refresh()
        progressBar?.visibility = View.VISIBLE
        Log.d("refresh", "HomeFragment, refresh()")
    }

}