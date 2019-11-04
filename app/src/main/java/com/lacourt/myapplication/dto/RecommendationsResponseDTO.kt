package com.lacourt.myapplication.dto

data class RecommendationsResponseDTO(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)