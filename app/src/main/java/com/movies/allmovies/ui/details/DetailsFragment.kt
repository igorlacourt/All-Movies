package com.movies.allmovies.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.bold
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.movies.allmovies.AppConstants

import com.movies.allmovies.domainmodel.Details
import com.movies.allmovies.viewmodel.DetailsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.Exception
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movies.allmovies.R
import com.movies.allmovies.databinding.FragmentDetailsBinding
import com.movies.allmovies.domainMappers.MapperFunctions
import com.movies.allmovies.domainMappers.toCastDTO
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.dto.CastsDTO
import com.movies.allmovies.openYoutube
import com.movies.allmovies.ui.GridAdapter
import com.movies.allmovies.ui.OnMovieClick
import com.movies.allmovies.util.BannerAds
import java.net.URLEncoder
import kotlin.math.floor

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment(), OnMovieClick, OnCastClick {
    lateinit var viewModel: DetailsViewModel
    private var movieTitle: String? = null

    var movieId: Int? = null

    private lateinit var binding: FragmentDetailsBinding
    val castsAdapter: CastsAdapter? = null
    val gridAdapter: GridAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_details, container, false
        )
        viewModel =
            ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        searchStreamOnGoogleClickListener()

        binding.rvRecommended.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        binding.rvRecommended.adapter = GridAdapter(context, this, ArrayList())

        binding.rvCasts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCasts.adapter = CastsAdapter(context, this, ArrayList())


        val id = arguments?.getInt("id") ?: 0

        var details: Details? = null



        viewModel.recommendedMovies.observe(viewLifecycleOwner, Observer { movies ->
            if (movies.isNullOrEmpty())
                binding.tvRecommendedEmpty.visibility = View.VISIBLE
            else
                binding.tvRecommendedEmpty.visibility = View.INVISIBLE
            gridAdapter?.setList(movies as List<DomainMovie>)
        })

        if (id != 0)
            viewModel.getDetails(id)
        else
            Toast.makeText(
                context,
                "id is NULL",
                Toast.LENGTH_LONG
            ).show()

        viewModel.movie.observe(viewLifecycleOwner, Observer {
            movieId = it.id
            details = it
            displayDetails(it)
            it.casts?.let { cast ->
                cast.cast?.let { actors ->
                    castsAdapter?.setList(actors)
                }
                cast.crew?.let { crew ->
                    castsAdapter?.addToList(crew.toCastDTO())
                }
            }
        })

        binding.detailBackdrop.setOnClickListener {
            details?.openYoutube(context)
        }

        binding.btAddToList.setOnClickListener {
            Log.d("log_is_inserted", "Button clicked")
            viewModel.addToList()
        }


        BannerAds.loadAds(context, binding.root)

        return binding.root
    }

    private fun searchStreamOnGoogleClickListener() {
        binding.btnSearchStreamOnGoogle.setOnClickListener {
            movieTitle?.let {title ->
                var escapedQuery = URLEncoder.encode("watch movie ${title}", "UTF-8")
                var uri = Uri.parse("https://www.google.com/#q=" + escapedQuery)
                var intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.isInDatabase(movieId)
    }

    fun displayDetails(details: Details?) {
        details?.apply {
            val imagePath = backdrop_path ?: poster_path
            Log.d("calltest", "onChange, response = $this")

            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W500}$imagePath")
                .placeholder(R.drawable.placeholder)
                .into(detail_backdrop, object : Callback {
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
            movieTitle = title
            binding.btnSearchStreamOnGoogle.visibility = View.VISIBLE
            detail_title.text = title
            detail_overview.text = overview
            release_year.text = release_date
            tv_duration.text = convertDuration(runtime)
            setVoteAverageColor(tv_vote_average, vote_average)
            binding.detailsProgressBar.visibility = View.INVISIBLE
            setDirector(tv_director, casts)
            setCast(tv_cast, casts)
        }

    }

    private fun setCast(tvCast: TextView, castAndDirector: CastsDTO?) {
        var builder = SpannableStringBuilder()

        builder.bold { append("Cast: ") }

        if (!castAndDirector?.cast.isNullOrEmpty()) {
            for (i in 0 until castAndDirector?.cast!!.size) {
                if (i == castAndDirector.cast.size - 1)
                    builder.append("${castAndDirector.cast[i].name}")
                else
                    builder.append("${castAndDirector.cast[i].name}, ")
            }
        }

        tvCast.text = builder
    }

    private fun setDirector(tvDir: TextView, castAndDirector: CastsDTO?) {
        var builder = SpannableStringBuilder()

        builder.bold { append("Director: ") }
        builder.append("${castAndDirector?.crew?.get(0)?.name}")

        tvDir.text = builder
    }

    private fun setVoteAverageColor(tv: TextView, avg: Double?) {
        if (avg != null) {
            var color = R.color.avg0until4
            val vote: Int = floor(avg).toInt()
            when (vote) {
                10 -> color = R.color.avg8until10
                9 -> color = R.color.avg8until10
                8 -> color = R.color.avg8until10
                7 -> color = R.color.avg6until8
                6 -> color = R.color.avg6until8
                5 -> color = R.color.avg4until6
                4 -> color = R.color.avg4until6
                3 -> color = R.color.avg0until4
                2 -> color = R.color.avg0until4
                1 -> color = R.color.avg0until4
            }
            tv.text = "${avg.toString()} vote average"
            context?.let { tv.setTextColor(ContextCompat.getColor(it, color)) }
        }
    }

    private fun convertDuration(timeSeconds: Int?) = timeSeconds?.let {
        val minutes = (it % 60)
        val hours = (it / 60)
        "${hours}h ${minutes}m"
    } ?: ""

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
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         */
//        @JvmStatic
//        fun newInstance(id: Int) =
//            DetailsFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(PARAM_ID, id)
//                }
//            }
//    }
}
