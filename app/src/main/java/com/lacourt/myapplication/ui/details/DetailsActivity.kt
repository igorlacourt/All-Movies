package com.lacourt.myapplication.ui.details

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.model.Movie
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        var movie = intent.getSerializableExtra("movie") as Movie

        movie.apply {
            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W500}$backdrop_path")
                .placeholder(R.drawable.clapperboard)
                .into(detail_backdrop, object : Callback {
                    override fun onSuccess() {
                        Toast.makeText(this@DetailsActivity, "Success", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Exception?) {
                        Toast.makeText(this@DetailsActivity, "Error loading image", Toast.LENGTH_LONG).show()
                    }

                })
            detail_title.text = title
            detail_overview.text = overview
        }

        wish_list_btn.setOnClickListener {

        }
    }
}
