package com.lacourt.myapplication.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "favorites")
data class Favorite(@Expose
                    @NonNull
                    @PrimaryKey(autoGenerate = false)
                    val id: Int,

                    @Expose
                    val poster_path: String?,

                    @Expose
                    val release_date: String?,

                    @Expose
                    val vote_average: Double?
)