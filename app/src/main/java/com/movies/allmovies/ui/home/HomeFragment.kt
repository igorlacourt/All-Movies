package com.movies.allmovies.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.movies.allmovies.MainActivity
import com.movies.allmovies.R
import com.movies.allmovies.epoxy.*
import com.movies.allmovies.network.Resource
import com.movies.allmovies.ui.OnItemClick
import com.movies.allmovies.viewmodel.HomeViewModel
import com.movies.allmovies.viewmodel.SearchViewModel
import javax.inject.Inject


class HomeFragment : Fragment(), OnItemClick {
//    private lateinit var viewModel: HomeViewModel

    // Dagger code
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    private lateinit var recyclerView: EpoxyRecyclerView

    private val itemClick = this as OnItemClick

    private val movieController by lazy { MovieController(context, itemClick, viewModel) }

    private var topTrendingMovieId: Int? = null

    var testString = "Test String"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("callstest", "onCreateView called\n")
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        Log.d("refreshLog", "onCreateView() called")

//        viewModel =
//            ViewModelProviders.of(this).get(HomeViewModel::class.java)

        recyclerView = root.findViewById(R.id.movie_list)

        Log.d("clicklog", "before initialize movieController")
        Log.d("genreslog", "HomeFragment, onCreateView() called")

        val adapter = movieController.adapter

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter

        viewModel.listsOfMovies?.observe(this, Observer { response ->
            response?.let { movieController.submitListsOfMovies(it, null) }
//            Log.d("parallelRequest", "HomeFragment, response size = ${response?.data?.size}")
//            when (response?.status) {
//                Resource.Status.SUCCESS -> {
//                    Log.d("listsLog", "HomeFragment, response size = ${response?.data?.size}")
//                    response.data?.let {
//                        Log.d(
//                            "refresh",
//                            "HomeFragment, listsOfMovies?.observe, success response = ${response.data.size}"
//                        )
//                        movieController.submitListsOfMovies(it, null)
//                    }
//                }
//                Resource.Status.LOADING -> {
//                }
//                Resource.Status.ERROR -> {
//                    Log.d(
//                        "refresh",
//                        "HomeFragment, listsOfMovies?.observe, fail, response = ${response.error?.cd}"
//                    )
//                    movieController.submitListsOfMovies(null, response.error)
//                    Toast.makeText(
//                        context,
//                        "${response.error?.message}, ${response.error?.cd}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
        })

        viewModel.topTrendingMovie?.observe(this, Observer { details ->
//            if (details.genres != null)
            Log.d("getTopMovie", "HomeFragment, topTrendingMovie?.observe, response = ${details.title}")
            topTrendingMovieId = details.id
            movieController.submitTopTrendingMovie(details, null)
        })

        viewModel.isInDatabase?.observe(this, Observer { isInDatabase ->
            movieController.submitIsInDatabase(isInDatabase)
        })

        viewModel.isLoading?.observe(this, Observer { isLoading ->
            movieController.submitIsLoading(isLoading)
        })

        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.isIndatabase(topTrendingMovieId)
    }

    override fun onItemClick(id: Int) {
        val homeToDetailsFragment =
            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(id)
        findNavController().navigate(homeToDetailsFragment)
    }

}