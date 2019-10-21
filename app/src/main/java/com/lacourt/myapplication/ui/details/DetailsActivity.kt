package com.lacourt.myapplication.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.domainMappers.MapperFunctions
import com.lacourt.myapplication.domainmodel.Details
import com.lacourt.myapplication.network.Resource
import com.lacourt.myapplication.viewmodel.DetailsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.fragment_mylist.*
import java.lang.Exception

class DetailsActivity : AppCompatActivity() {
    lateinit var viewModel:DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        datails_progress_bar.visibility = View.VISIBLE

        val id = intent.getIntExtra("id", 0)

        viewModel =
            ViewModelProviders.of(this).get(DetailsViewModel::class.java)


        viewModel.isInDatabase.observe(this, Observer {isInDatabase ->
            Log.d("log_is_inserted", "onChanged()")
            if (isInDatabase) {
                wish_list_btn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_check_mark_24dp, null))
                Log.d("log_is_inserted", "isInserted true, button to checkmark")
            }
            else {
                wish_list_btn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.wish_list_btn_24dp, null))
                Log.d("log_is_inserted", "isInserted false, button to plus sign")
            }
        })

        if (id != 0) viewModel.getDetails(id) else Toast.makeText(
            this,
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
                    datails_progress_bar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    //Display error message
                }
            }
        })

        wish_list_btn.setOnClickListener {
            Log.d("log_is_inserted", "Button clicked")
            if(viewModel.isInDatabase.value == false) {
                Log.d("log_is_inserted", "isInDatabase false")
                val myListItem = viewModel.movie?.value?.let {
                    it.data?.let { details ->
                        MapperFunctions.toMyListItem(
                            details
                        )
                    }
                }

                if (myListItem != null) {
                    viewModel.insert(myListItem)
                } else {
                    Toast.makeText(
                        this@DetailsActivity,
                        "Did not save to My List.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else {
                Log.d("log_is_inserted", "isInDatabase true")
                viewModel.movie?.value?.data?.id?.let { id -> viewModel.delete(id) }
            }
        }
    }

    fun displayDetails(details: Details?){
        details?.apply {
            var imagePath = backdrop_path ?: poster_path
            Log.d("calltest", "onChange, response = $this")

            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W500}$imagePath")
                .placeholder(R.drawable.clapperboard)
                .into(detail_backdrop, object : Callback {
                    override fun onSuccess() {
                        Toast.makeText(
                            this@DetailsActivity,
                            "Image loaded",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onError(e: Exception?) {
                        Toast.makeText(
                            this@DetailsActivity,
                            "Error loading image",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            detail_title.text = title
            detail_overview.text = overview
            release_year.text = release_date
            datails_progress_bar.visibility = View.INVISIBLE
        }
    }
}
