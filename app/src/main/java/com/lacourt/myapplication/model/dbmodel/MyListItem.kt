package com.lacourt.myapplication.model.dbmodel

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "mylist")
data class MyListItem(@Expose
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