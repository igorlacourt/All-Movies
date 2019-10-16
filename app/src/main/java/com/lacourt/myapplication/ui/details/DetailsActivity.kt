package com.lacourt.myapplication.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.domainMappers.MapperFunctions
import com.lacourt.myapplication.viewmodel.DetailsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        datails_progress_bar.visibility = View.VISIBLE

        val id = intent.getIntExtra("id", 0)

        val viewModel =
            ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        if (id != 0) viewModel.getDetails(id) else Toast.makeText(
            this,
            "id is NULL",
            Toast.LENGTH_LONG
        ).show()

        viewModel.movie?.observe(this, Observer {
            Log.d("calltest", "onChange called, response = $it")
            it?.apply {
                var imagePath = data?.backdrop_path ?: data?.poster_path
                Log.d("calltest", "onChange, response = $it")
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
//                detail_title.text = data?.title
//                detail_overview.text = data?.overview
                detail_title.text = data?.title
                detail_overview.text = data?.overview
                release_year.text = data?.release_date
                datails_progress_bar.visibility = View.INVISIBLE
            }
        })

        wish_list_btn.setOnClickListener {
//            val myListItem = viewModel.movie?.value?.let { MapperFunctions.toMyListItem(it) }
//
//            if (myListItem != null) {
//                viewModel.insert(myListItem)
//            } else {
//                Toast.makeText(
//                    this@DetailsActivity,
//                    "Did not save to My List.",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
        }
    }
}
