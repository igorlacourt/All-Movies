package com.movies.allmovies.domainmodel

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mylist")
data class MyListItem(
    @NonNull
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val poster_path: String?,
    val backdrop_path: String?
)