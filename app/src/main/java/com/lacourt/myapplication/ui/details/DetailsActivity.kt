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
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.Mapper
import com.lacourt.myapplication.model.Favorite
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.model.dbMovie.DbMovie
import com.lacourt.myapplication.model.domainmodel.DomainModel
import com.lacourt.myapplication.viewmodel.DetailsViewModel
import com.lacourt.myapplication.viewmodel.HomeViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

class DetailsActivity : AppCompatActivity(), Mapper<DomainModel, DbMovie> {
    private val favoriteDao =
        AppDatabase.getDatabase(application)!!.FavoriteDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val id = intent.getIntExtra("id", 0)

        val viewModel =
            ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        if (id != 0) viewModel.fetchDetails(id) else Toast.makeText(this, "id is NULL", Toast.LENGTH_LONG).show()

        viewModel.movie?.observe(this, Observer  {
            it.apply {
                Picasso.get()
                    .load("${AppConstants.TMDB_IMAGE_BASE_URL_W500}$backdrop_path")
                    .placeholder(R.drawable.clapperboard)
                    .into(detail_backdrop, object : Callback {
                        override fun onSuccess() {
                            Toast.makeText(this@DetailsActivity, "Image loaded", Toast.LENGTH_LONG).show()
                        }

                        override fun onError(e: Exception?) {
                            Toast.makeText(this@DetailsActivity, "Error loading image", Toast.LENGTH_LONG).show()
                        }

                    })
                detail_title.text = title
                detail_overview.text = overview
            }
        })

        wish_list_btn.setOnClickListener {
            val dbMovie = viewModel.movie?.value?.let { map(it) }
            if (dbMovie != null) {
                favoriteDao.insert(dbMovie)
            }
        }
    }

    override fun map(input: DomainModel): DbMovie {
        return with(input){
            DbMovie(id, poster_path, release_date, vote_average)
        }
    }
}
