package com.lacourt.myapplication.domainmodel
import com.google.gson.annotations.Expose

data class DomainMovie  (
    val id: Int?,

    @Expose
    val poster_path: String?
)