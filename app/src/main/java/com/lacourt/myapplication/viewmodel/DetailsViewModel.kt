package com.lacourt.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lacourt.myapplication.database.AppDatabase
import com.lacourt.myapplication.domainMappers.Mapper
import com.lacourt.myapplication.model.dbmodel.MyListItem
import com.lacourt.myapplication.model.dto.Details
import com.lacourt.myapplication.model.domainmodel.DomainModel
import com.lacourt.myapplication.repository.DetailsRepository

class DetailsViewModel(application: Application) : AndroidViewModel(application), Mapper<Details, DomainModel> {
    internal var movie: LiveData<DomainModel>? = null
    private val repository: DetailsRepository = DetailsRepository(application, this)

    init {
        movie = repository.movie
    }

    fun insert(myListItem: MyListItem) {
        repository.insert(myListItem)
    }

    fun fetchDetails(id: Int) {
        repository.fetchDetails(id)
    }

    override fun map(input: Details): DomainModel {
        return detailsToDomain(input)
    }

    private fun detailsToDomain(input: Details): DomainModel {
        return with(input) {
            DomainModel(
                backdrop_path,
                genres,
                id,
                overview,
                poster_path,
                release_date,
                title,
                vote_average
            )
        }
    }
}