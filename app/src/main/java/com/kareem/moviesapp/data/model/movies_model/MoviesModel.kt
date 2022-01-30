package com.kareem.moviesapp.data.model.movies_model


data class MoviesModel(
    var dates: Dates? = null,
    var page: Int = 1,
    var results: List<Movie>? = null,
    var total_pages: Int? = null,
    var total_results: Int? = null
)