package com.kareem.moviesapp.data.model.reviews

data class ReviewsModel(
    var id: Int? = null,
    var page: Int? = null,
    var results: List<Review>? = null,
    var total_pages: Int? = null,
    var total_results: Int? = null
)