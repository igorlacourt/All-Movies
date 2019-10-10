package com.lacourt.myapplication.ui.details

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.viewmodel.DetailsViewModel
import com.lacourt.myapplication.viewmodel.HomeViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        var id = intent.getIntExtra("id", 0)

        val viewModel =
            ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        if (id != 0) viewModel.fetchDetails(id) else Toast.makeText(this, "id is NULL", Toast.LENGTH_LONG).show()

        viewModel.movie?.observe(this, Observer  {
            it.apply {
                Log.d("testdetails", "onchange called")
                Log.d("testdetails", "domainModel: title = $title")
                Picasso.get()
                    .load("${AppConstants.TMDB_IMAGE_BASE_URL_W500}$backdrop_path")
                    .placeholder(R.drawable.clapperboard)
                    .into(detail_backdrop, object : Callback {
                        override fun onSuccess() {
                            Log.d("testdetails", "Image loaded")
                            Toast.makeText(this@DetailsActivity, "Image loaded", Toast.LENGTH_LONG).show()
                        }

                        override fun onError(e: Exception?) {
                            Log.d("testdetails", "Error loading image")
                            Toast.makeText(this@DetailsActivity, "Error loading image", Toast.LENGTH_LONG).show()
                        }

                    })
                detail_title.text = title
                detail_overview.text = overview
            }
        })

        wish_list_btn.setOnClickListener {

        }
    }
}
