package com.lacourt.myapplication.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.lacourt.myapplication.R
import com.lacourt.myapplication.domainmodel.DomainMovie
import com.lacourt.myapplication.epoxy.*
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.ui.OnItemClick
import com.lacourt.myapplication.viewmodel.HomeViewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.core.os.HandlerCompat.postDelayed
import android.os.Handler
import kotlinx.android.synthetic.main.top_trending_movie.*


class HomeFragment : Fragment(), OnItemClick {
    private lateinit var viewModel: HomeViewModel
    private lateinit var recyclerView: EpoxyRecyclerView

    private val itemClick = this as OnItemClick

    private val movieController by lazy { MovieController(context, itemClick, viewModel) }

    private var topTrendingMovieId: Int? = null

    private var homeToolbar: ConstraintLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("callstest", "onCreateView called\n")
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        homeToolbar = root.findViewById<ConstraintLayout>(R.id.home_toolbar)

        Log.d("refreshLog", "onCreateView() called")

        viewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)

        recyclerView = root.findViewById(R.id.movie_list)

        Log.d("clicklog", "before initialize movieController")
        Log.d("genreslog", "HomeFragment, onCreateView() called")

        val adapter = movieController.adapter

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter

        viewModel.listsOfMovies?.observe(this, Observer { response ->
            when (response?.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("listsLog", "HomeFragment, response size = ${response?.data?.size}")
                    response.data?.let {
                        Log.d(
                            "refresh",
                            "HomeFragment, listsOfMovies?.observe, success response = ${response.data.size}"
                        )
                        movieController.submitListsOfMovies(it, null)
//                        movieController.requestModelBuild()
//                        recyclerView.setController(movieController)
                    }
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                    Log.d(
                        "refresh",
                        "HomeFragment, listsOfMovies?.observe, fail, response = ${response.error?.cd}"
                    )
                    movieController.submitListsOfMovies(null, response.error)
                    Toast.makeText(
                        context,
                        "${response.error?.message}, ${response.error?.cd}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

        viewModel.topTrendingMovie?.observe(this, Observer { details ->
            when (details.status) {
                Resource.Status.SUCCESS -> {
                    details.data?.let {
                        if (it.genres != null)
                            Log.d(
                                "refresh",
                                "HomeFragment, topTrendingMovie?.observe, success, response = ${it.title}"
                            )
                        topTrendingMovieId = details.data.id
                        movieController.submitTopTrendingMovie(it, null)
//                        movieController.requestModelBuild()
//                        recyclerView.setController(movieController)
                    }
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                    Log.d(
                        "refresh",
                        "HomeFragment, topTrendingMovie?.observe, fail, response = ${details.error?.cd}"
                    )
                    movieController.submitTopTrendingMovie(null, details.error)
                }
            }
        })

        viewModel.isInDatabase?.observe(this, Observer { isInDatabase ->
            movieController.submitIsInDatabase(isInDatabase)
//            movieController.requestModelBuild()
//            recyclerView.setController(movieController)
        })

        viewModel.isLoading?.observe(this, Observer { isLoading ->
            movieController.submitIsLoading(isLoading)
//            movieController.requestModelBuild()
//            recyclerView.setController(movieController)


        })

        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.isIndatabase(topTrendingMovieId)

        homeToolbar?.y = 0.0f
        homeToolbar?.visibility = View.VISIBLE

        var initY: Int? = null
        var delta: Int = 0
        if (homeToolbar != null && homeToolbar?.isVisible != null) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (initY == null) {
                        initY = dy
                        homeToolbar!!.y = 0.0f
                    }
                    delta = initY!! - dy

                    Log.d("myoffset", "delta = $delta")
                    Log.d("myoffset", "homeToolbar.y = ${homeToolbar!!.y}")

                    if (delta < 0) {
                        if (homeToolbar!!.y < -homeToolbar!!.height) {
                            homeToolbar!!.y = -homeToolbar!!.height.toFloat()
                        } else {
                            homeToolbar!!.y = homeToolbar!!.y + (delta / 2)
                        }
                    } else {
                        if (homeToolbar!!.y < 0.0f) {
                            homeToolbar!!.y = homeToolbar!!.y + (delta / 2)
                        } else {
                            homeToolbar!!.y = 0.0f
                        }
                    }
                    Log.d("myoffset", "homeToolbar.y - (delta / 4) = ${homeToolbar!!.y}")
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }

    }

    private fun toolbarBehavior() {

    }

    override fun onItemClick(id: Int) {
        val homeToDetailsFragment =
            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(id)
        findNavController().navigate(homeToDetailsFragment)
    }

}