package com.movies.allmovies.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.movies.allmovies.AppConstants
import com.movies.allmovies.MainActivity
import com.movies.allmovies.R
import com.movies.allmovies.databinding.FragmentHomeMoviesBinding
import com.movies.allmovies.domainMappers.toMyListItem
import com.movies.allmovies.epoxy.*
import com.movies.allmovies.ui.OnItemClick
import com.movies.allmovies.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.add_to_my_list.view.*
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

    private var topTrendingMovieId: Int? = null
    private lateinit var binding: FragmentHomeMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home_movies, container, false
        )
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        attachObservers()
        attachClickListeners()

        return binding.root
    }

    private fun attachClickListeners() {
        binding.iBtnAddToMyList.btAddToList.setOnClickListener {
            viewModel.topTrendingMovie?.value?.toMyListItem()?.let {
                viewModel.insert(it)
            }
        }
    }

    private fun attachObservers() {
        listsOfMoviesObserver()
        topMovieObserver()
        isInDataBaseObserver()
        isLoadingObserver()
    }

    private fun listsOfMoviesObserver() {
        viewModel.listsOfMovies.observe(this, Observer { response ->
            response?.let {
                binding.rvListsOfMovies.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvListsOfMovies.adapter = ListsOfMoviesAdapter(requireContext(), response)
            }
        })
    }

    private fun topMovieObserver() {
        viewModel.topTrendingMovie?.observe(this, Observer { details ->
            topTrendingMovieId = details.id
            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W780}${details.backdrop_path}")
                .placeholder(R.drawable.placeholder)
                .into(binding.ivTopTrendingMovie)
        })
    }

    private fun isInDataBaseObserver() {
        viewModel.isInDatabase.observe(this, Observer { isInDatabase ->
            if (isInDatabase) {
                binding.iBtnAddToMyList.btAddToList.setImageDrawable(
                    ResourcesCompat.getDrawable(resources, R.drawable.wish_list_btn_24dp, null)
                )
            } else {
                ResourcesCompat.getDrawable(resources, R.drawable.ic_check_mark_24dp, null)
            }
        })
    }

    private fun isLoadingObserver() {
        viewModel.isLoading.observe(this, Observer { isLoading ->
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.isInDatabase(topTrendingMovieId)
    }

    override fun onItemClick(id: Int) {
        val homeToDetailsFragment =
            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(id)
        findNavController().navigate(homeToDetailsFragment)
    }

}