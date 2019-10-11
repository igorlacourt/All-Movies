package com.lacourt.myapplication.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lacourt.myapplication.AppConstants
import com.lacourt.myapplication.R
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.Mapper
import com.lacourt.myapplication.model.dbmodel.DbMovie
import com.lacourt.myapplication.model.dbmodel.MyListItem
import com.lacourt.myapplication.model.domainmodel.DomainModel
import com.lacourt.myapplication.viewmodel.DetailsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

class DetailsActivity : AppCompatActivity(), Mapper<DomainModel, MyListItem> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val id = intent.getIntExtra("id", 0)

        val viewModel =
            ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        if (id != 0) viewModel.fetchDetails(id) else Toast.makeText(
            this,
            "id is NULL",
            Toast.LENGTH_LONG
        ).show()

        viewModel.movie?.observe(this, Observer {
            it.apply {
                var imagePath = backdrop_path ?: poster_path

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
            }
        })

        wish_list_btn.setOnClickListener {
            val myListItem = viewModel.movie?.value?.let { map(it) }
            if (myListItem != null) {
                viewModel.insert(myListItem)
            } else {
                Toast.makeText(
                    this@DetailsActivity,
                    "Did not save to wish list.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun map(input: DomainModel): MyListItem {
        return with(input) {
            MyListItem(id, poster_path, release_date, vote_average)
        }
    }
}
