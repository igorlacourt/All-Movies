package com.lacourt.myapplication.dto

import com.google.gson.annotations.Expose

data class CastsDTO(
    @Expose
    val cast: ArrayList<CastDTO>?,
    @Expose
    val crew: ArrayList<CrewDTO>?
)