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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.ui.MovieAdapter
import com.lacourt.myapplication.ui.OnMovieClick
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
        Log.d("callstest", "onCreateView called.\n")
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        /*An 'activity' is passed in to '.of()' method below, because when passing
        'this', the ViewModel's init block is called twice.*/
        homeViewModel =
            ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
        Log.d("callstest", "homeViewModel initialized.\n")

        val progressBar: ProgressBar = root.findViewById(R.id.progress_circular)
        recyclerView = root.findViewById(R.id.movie_list)
        val adapter = MovieAdapter(context, onMovieClick)

        recyclerView.computeVerticalScrollOffset()

        var gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = gridLayoutManager

        recyclerView.adapter = adapter
        progressBar.visibility = View.VISIBLE

        (recyclerView.adapter as MovieAdapter).registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })

        homeViewModel.movies.observe(this, Observer { pagedList ->
            adapter.submitList(pagedList)
            progressBar.visibility = View.INVISIBLE

            //TODO pagedList fetchs the entiry database and not the 50 it should fetch
            Log.d("testorder", ".observe: list = ${pagedList?.size}, dbCount = ${homeViewModel.movieDao.getCount()}")
        })

        return root
    }

    fun orderDateAsc() {
        homeViewModel.rearrengeMovies(AppConstants.DATE_ASC)
        Log.d("testorder", "orderDateAsc()")
    }

    fun orderDateDesc() {
        homeViewModel.rearrengeMovies(AppConstants.DATE_DESC)
        Log.d("testorder", "orderDateDesc()")
    }

    override fun onMovieClick(movie: Movie) {
        Toast.makeText(context, "${movie.title}", Toast.LENGTH_SHORT).show()
    }
}
