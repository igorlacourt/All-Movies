package com.movies.allmovies.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.set
import androidx.core.text.toSpannable
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
import com.movies.allmovies.domainmappers.toMyListItem
import com.movies.allmovies.dto.GenreXDTO
import com.movies.allmovies.dto.VideoDTO
import com.movies.allmovies.ui.OnMovieClick
import com.movies.allmovies.util.BannerAds
import com.movies.allmovies.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

class HomeFragment : Fragment(), OnMovieClick {
    // Dagger code
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    private lateinit var bannerAds: BannerAds
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

        bannerAds = BannerAds(binding.root)
        bannerAds.loadAds(requireContext())

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerAds.removeAds()
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
                binding.rvListsOfMovies.adapter = ListsOfMoviesAdapter(this, requireContext(), response)
            }
        })
    }

    private fun topMovieObserver() {
        viewModel.topTrendingMovie?.observe(viewLifecycleOwner, Observer { details ->
            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W780}${details.backdrop_path}")
                .placeholder(R.drawable.placeholder)
                .into(binding.ivTopTrendingMovie)
            binding.tvTopTrendingMovieTitle.text = details.title
            details.genres?.let { drawGenres(it) }
            details?.id?.let { id -> viewModel.isTopMovieInDatabase(id) }
        })
    }

    private fun isLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
        })
    }

    private fun attachClickListeners() {
        addToMyListClickListener()
        trailerButtonClickListener()
        learnMoreButtonClickListener()
        topMovieImageClickListener()
        attachRetryClickListener()
    }

    private fun topMovieImageClickListener() {
        binding.ivTopTrendingMovie.setOnClickListener {
            goToTrailer(viewModel.topTrendingMovie?.value?.videos)
        }
    }

    private fun addToMyListClickListener() {
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

    private fun trailerButtonClickListener() {
        binding.btnTopTrendingTrailer.setOnClickListener {
            goToTrailer(viewModel.topTrendingMovie?.value?.videos)
        }
    }

    private fun learnMoreButtonClickListener() {
        binding.btnLearnMore.setOnClickListener {
            viewModel.topTrendingMovie?.value?.id?.let { id ->
                goToDetailsFragment(id)
            }
        }
    }

    private fun attachRetryClickListener() {
        binding.iErrorScreen.btRetry.setOnClickListener {
            viewModel.getListOfMovies()
        }
    }

    private fun drawGenres(genresList: List<GenreXDTO>){
        for (i in genresList.indices) {
            binding.tvGenres.append(genresList[i].name)
            if (i < genresList.size - 1) {
                val s = "  •  ".toSpannable()
                var color = context?.resources?.getColor(R.color.genreFamilyColor)
                genresList.map {
                    when (it.name) {
                        "Drama" -> color = context?.resources?.getColor(R.color.genreDramaColor)
                        "Action" -> color = context?.resources?.getColor(R.color.genreActionColor)
                        "Western" -> color = context?.resources?.getColor(R.color.genreWesternColor)
                        "Thriller" -> color =
                            context?.resources?.getColor(R.color.genreThrillerColor)
                        "War" -> color = context?.resources?.getColor(R.color.genreWarColor)
                        "Horror" -> color = context?.resources?.getColor(R.color.genreHorrorColor)
                        "Terror" -> color = context?.resources?.getColor(R.color.genreTerrorColor)
                        "Documentary" -> color =
                            context?.resources?.getColor(R.color.genreDocumentaryColor)
                    }
                }

                s[0..5] = color?.let { ForegroundColorSpan(it) } ?: ForegroundColorSpan(Color.GRAY)

                binding.tvGenres.append(s)
            }

        }
    }

    override fun onClick(id: Int) {
       goToDetailsFragment(id)
    }

    private fun goToTrailer(videos: ArrayList<VideoDTO>?) {
        if (!videos.isNullOrEmpty() && !videos[0].key.isNullOrEmpty()) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=${videos[0].key}")
            )
            context?.startActivity(webIntent)
        } else {
            Toast.makeText(
                context,
                context?.getString(R.string.no_video_to_show),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun goToDetailsFragment(id: Int) {
        val homeToDetailsFragment =
            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(id)
        findNavController().navigate(homeToDetailsFragment)
    }
}