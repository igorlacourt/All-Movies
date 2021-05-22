package com.movies.allmovies.ui.cast

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movies.allmovies.AppConstants
import com.movies.allmovies.MainActivity
import com.movies.allmovies.R
import com.movies.allmovies.dto.PersonDetails
import com.movies.allmovies.network.Resource
import com.movies.allmovies.ui.GridAdapter
import com.movies.allmovies.ui.OnMovieClick
import com.movies.allmovies.util.BannerAds
import com.movies.allmovies.viewmodel.PersonViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_person_details.*
import javax.inject.Inject

class PersonDetailsFragment : Fragment(), OnMovieClick {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PersonViewModel> { viewModelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    lateinit var progressBar: FrameLayout

    lateinit var bannerAds: BannerAds

    var personId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_person_details, container, false)
//        voteAverage = view.findViewById(R.id.tv_vote_average)
//        backdropImageView = view.findViewById(R.id.detail_backdrop)
        progressBar = view.findViewById(R.id.person_details_progress_bar)
        progressBar.visibility = View.VISIBLE
//        emptyRecomendations = view.findViewById(R.id.tv_recommended_empty)
//        emptyRecomendations.visibility = View.VISIBLE

        var recyclerView = view.findViewById<RecyclerView>(R.id.rv_casts)
        val adapter = GridAdapter(context, this, ArrayList())
        recyclerView.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        val id = arguments?.getInt("id") ?: 0

        var details: PersonDetails? = null


        if (id != 0) {
            viewModel.getPersonDetails(id)
            viewModel.getActorsMovies(id)
        }
        else {
            Toast.makeText(context, "Can not load details", Toast.LENGTH_LONG).show()
        }

        viewModel.starredMovies.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    resource?.data?.let { movies ->
                        if (movies.isNullOrEmpty())
//                            emptyRecomendations.visibility = View.VISIBLE
                        else
//                            emptyRecomendations.visibility = View.INVISIBLE

//                        Log.d("recnull", "visibility = ${emptyRecomendations.visibility}")
                        adapter.setList(movies)
//                        recommendedMoviesAdapter.setList(movies)
                    }
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                }
            }
        })

        viewModel.person?.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("personlog", "Resource.Status.SUCCESS")
                    personId = it.data?.id
                    details = it.data
                    displayDetails(it.data)
                    progressBar.visibility = View.INVISIBLE
                }
                Resource.Status.LOADING -> {
                    // A return is given only once, so it is SUCCESS or ERROR. This loading case may not be necessary.
                    Log.d("personlog", "Resource.Status.LOADING")
                    progressBar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    //Display error message
                    Log.d("personlog", "Resource.Status.ERROR")
                }
            }
        })

        bannerAds = BannerAds(view)
        bannerAds.loadAds(requireContext())

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerAds.removeAds()
    }

    fun displayDetails(details: PersonDetails?) {
        details?.apply {
            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W500}$profilePath")
                .placeholder(R.drawable.placeholder)
                .into(iv_profile, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception?) {
                        Toast.makeText(
                            context,
                            "Error loading image",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            tv_person_name.text = name
            tv_starred_movies.text = "Movies with $name"
            tv_bio.text = "$biography "
            tv_vote_average.text = "popularity $popularity"
            tv_duration.text = "${age()} years old"
            release_year.text = ""
            progressBar.visibility = View.INVISIBLE
        }

    }

    override fun onClick(id: Int) {
        if (id != 0) {
            Log.d("clickgrid", "HomeFragment, onItemClick, id = $id")
            val personDetailsToDetailsFragment = PersonDetailsFragmentDirections.actionPersonDetailsFragmentToDetailsFragment(id)
            findNavController().navigate(personDetailsToDetailsFragment)
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onItemClick(id: Int) {
//        if (id != 0) {
//            Log.d("clickgrid", "HomeFragment, onItemClick, id = $id")
//            val detailsToDetailsFragment = DetailsFragmentDirections.actionDetailsFragmentSelf(id)
//            findNavController().navigate(detailsToDetailsFragment)
//        } else {
//            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
//        }
//    }

}
