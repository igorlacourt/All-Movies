package com.lacourt.myapplication.ui.details

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lacourt.myapplication.AppConstants

import com.lacourt.myapplication.R
import com.lacourt.myapplication.domainMappers.MapperFunctions
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.viewmodel.DetailsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


private const val PARAM_ID = "param_id"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    lateinit var viewModel: DetailsViewModel
    lateinit var progressBar: ProgressBar
    lateinit var wishListButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        wishListButton = view.findViewById(R.id.wish_list_btn)
        progressBar = view.findViewById(R.id.details_progress_bar)
        progressBar.visibility = View.VISIBLE

        val id = arguments?.getInt("id") ?: 0


        viewModel =
            ViewModelProviders.of(this).get(DetailsViewModel::class.java)


        viewModel.isInDatabase.observe(this, Observer { isInDatabase ->
            Log.d("log_is_inserted", "onChanged()")
            if (isInDatabase) {
                wishListButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_check_mark_24dp,
                        null
                    )
                )
                Log.d("log_is_inserted", "isInserted true, button to checkmark")
            } else {
                wishListButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.wish_list_btn_24dp,
                        null
                    )
                )
                Log.d("log_is_inserted", "isInserted false, button to plus sign")
            }
        })

        if (id != 0)
            viewModel.getDetails(id)
        else
            Toast.makeText(
                context,
                "id is NULL",
                Toast.LENGTH_LONG
            ).show()

        viewModel.movie?.observe(this, Observer {
            when (it?.status) {
                Resource.Status.SUCCESS -> {
                    displayDetails(it.data)
                }
                Resource.Status.LOADING -> {
                    // A return is given only once, so it is SUCCESS or ERROR. This loading case may not be necessary.
                    progressBar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    //Display error message
                }
            }
        })

        wishListButton.setOnClickListener {
            Log.d("log_is_inserted", "Button clicked")
            if (viewModel.isInDatabase.value == false) {
                Log.d("log_is_inserted", "isInDatabase false")
                val itemData = viewModel.movie?.value?.data
                if (itemData?.id != null) {
                    viewModel.insert(
                        MapperFunctions.toMyListItem(
                            itemData
                        )
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Did not save to My List.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Log.d("log_is_inserted", "isInDatabase true")
                viewModel.movie?.value?.data?.id?.let { id -> viewModel.delete(id) }
            }
        }
        return view
    }

    fun displayDetails(details: Details?) {
        details?.apply {
            var imagePath = backdrop_path ?: poster_path
            Log.d("calltest", "onChange, response = $this")

            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W500}$imagePath")
                .placeholder(R.drawable.clapperboard)
                .into(detail_backdrop, object : Callback {
                    override fun onSuccess() {
                        Toast.makeText(
                            context,
                            "Image loaded",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onError(e: Exception?) {
                        Toast.makeText(
                            context,
                            "Error loading image",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            detail_title.text = title
            detail_overview.text = overview
            release_year.text = release_date
            tv_duration.text = convertDuration(runtime)
            tv_rate_details.text = "${vote_average.toString()} vote average"

            progressBar.visibility = View.INVISIBLE
        }

    }

    private fun convertDuration(timeSeconds: Int?) = timeSeconds?.let {
        val minutes = (it % 60)
        val hours = (it / 60)
        "${hours}h ${minutes}m"
    } ?: ""

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
