package com.movies.allmovies.ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.movies.allmovies.AppConstants
import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.viewmodel.DetailsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.Exception
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.movies.allmovies.MainActivity
import com.movies.allmovies.R
import com.movies.allmovies.databinding.FragmentDetailsBinding
import com.movies.allmovies.domainmappers.toCastDTO
import com.movies.allmovies.dto.CastsDTO
import com.movies.allmovies.openYoutube
import com.movies.allmovies.ui.GridAdapter
import com.movies.allmovies.ui.OnMovieClick
import com.movies.allmovies.util.BannerAds
import java.net.URLEncoder
import javax.inject.Inject

class DetailsFragment : Fragment(), OnMovieClick, OnCastClick {
//    lateinit var viewModel: DetailsViewModel
    private lateinit var binding: FragmentDetailsBinding
    private var castsAdapter: CastsAdapter? = null
    private var gridAdapter: GridAdapter? = null
    private var details: Details? = null

    private val args: DetailsFragmentArgs by navArgs()

    // Dagger code
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<DetailsViewModel> { viewModelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_details, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        attachObservers()
        setupAdapters()

        viewModel.getDetails(args.id)

        setupClickListeners()
        BannerAds.loadAds(context, binding.root)

        return binding.root
    }

    private fun attachObservers() {
        attachMovieObserver()
        attachRecommendedObserver()
    }

    private fun attachMovieObserver() {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            details = it
            displayDetails(it)
            setCastsList(it.casts)
        })
    }

    private fun attachRecommendedObserver() {
        viewModel.recommendedMovies.observe(viewLifecycleOwner, Observer { movies ->
            if (movies.isNullOrEmpty()) {
                binding.tvRecommendedEmpty.visibility = View.VISIBLE
            } else {
                binding.tvRecommendedEmpty.visibility = View.INVISIBLE
            }
            gridAdapter?.setList(movies)
        })
    }

    private fun setupAdapters() {
        setupRecommendedAdapter()
        setupCastsAdapter()
    }

    private fun setupRecommendedAdapter() {
        binding.rvRecommended.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        gridAdapter = GridAdapter(context, this, ArrayList())
        binding.rvRecommended.adapter = gridAdapter
    }

    private fun setupCastsAdapter() {
        binding.rvCasts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        castsAdapter = CastsAdapter(context, this, ArrayList())
        binding.rvCasts.adapter = castsAdapter
    }

    private fun setupClickListeners() {
        searchStreamOnGoogleClickListener()
        backdropClickListener()
        addToListClickListener()
    }

    private fun searchStreamOnGoogleClickListener() {
        binding.btnSearchStreamOnGoogle.setOnClickListener {
            viewModel.movie.value?.title?.let {title ->
                var escapedQuery = URLEncoder.encode("watch movie $title", "UTF-8")
                var uri = Uri.parse("https://www.google.com/#q=$escapedQuery")
                var intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }

    private fun backdropClickListener() {
        binding.detailBackdrop.setOnClickListener {
            details?.openYoutube(context)
        }
    }

    private fun addToListClickListener() {
        binding.btAddToList.setOnClickListener {
            viewModel.addToList()
        }
    }

    private fun setCastsList(casts: CastsDTO?) {
        casts?.let { cast ->
            cast.cast?.let { actors ->
                castsAdapter?.setList(actors)
            }
            cast.crew?.let { crew ->
                castsAdapter?.addToList(crew.toCastDTO())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.isInDatabase(args.id)
    }

    private fun displayDetails(details: Details?) {
        details?.apply {
            val imagePath = backdrop_path ?: poster_path

            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W500}$imagePath")
                .placeholder(R.drawable.placeholder)
                .into(detail_backdrop, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception?) {
                        Toast.makeText(context, "Error loading image", Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }

    }

    override fun onClick(id: Int) {
        if (id != 0) {
            Log.d("clickgrid", "HomeFragment, onItemClick, id = $id")
            val detailsToDetailsFragment = DetailsFragmentDirections.actionDetailsFragmentSelf(id)
            findNavController().navigate(detailsToDetailsFragment)
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCastClick(id: Int) {
        if (id != 0) {
            Log.d("clickgrid", "HomeFragment, onItemClick, id = $id")
            val detailsToPersonDetailsFragment = DetailsFragmentDirections.actionDetailsFragmentToPersonDetailFragment(id)
            findNavController().navigate(detailsToPersonDetailsFragment)
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
        }
    }
}
