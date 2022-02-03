package com.kareem.moviesapp.data.cache.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.reviews.Review

data class MovieWithReviews (
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "id",
        entityColumn = "movie_id"
    )
    val reviews:List<Review>

        )