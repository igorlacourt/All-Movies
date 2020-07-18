package com.movies.allmovies.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.movies.allmovies.AppConstants
import com.movies.allmovies.MainActivity
import com.movies.allmovies.R
import com.movies.allmovies.databinding.FragmentHomeBinding
import com.movies.allmovies.domainMappers.toMyListItem
import com.movies.allmovies.ui.OnItemClick
import com.movies.allmovies.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

class HomeFragment : Fragment(), OnItemClick {
    // Dagger code
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        attachObservers()
        attachClickListeners()

        return binding.root
    }

    private fun attachObservers() {
        listsOfMoviesObserver()
        topMovieObserver()
        isLoadingObserver()
    }

    private fun listsOfMoviesObserver() {
        viewModel.listsOfMovies.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                binding.rvListsOfMovies.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvListsOfMovies.adapter = ListsOfMoviesAdapter(requireContext(), response)
            }
        })
    }

    private fun topMovieObserver() {
        viewModel.topTrendingMovie?.observe(viewLifecycleOwner, Observer { details ->
            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W780}${details.backdrop_path}")
                .placeholder(R.drawable.placeholder)
                .into(binding.ivTopTrendingMovie)
            details?.id?.let { id -> viewModel.isTopMovieInDatabase(id) }
        })
    }

    private fun isLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
        })
    }

    private fun attachClickListeners() {
        binding.iAddToMyList.btAddToList.setOnClickListener {
            viewModel.topTrendingMovie?.value?.toMyListItem()?.let { listItem ->
                if(viewModel.isInDatabase.value == false){
                    viewModel.insert(listItem)
                } else {
                    listItem.id?.let { id -> viewModel.delete(id) }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onItemClick(id: Int) {
        val homeToDetailsFragment =
            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(id)
        findNavController().navigate(homeToDetailsFragment)
    }

}