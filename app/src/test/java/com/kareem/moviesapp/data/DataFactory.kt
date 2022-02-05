package com.kareem.moviesapp.data

import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.movies_model.MoviesModel
import com.kareem.moviesapp.data.model.reviews.Review
import com.kareem.moviesapp.data.model.reviews.ReviewsModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody


val moviesMockedList=
        listOf(
                Movie(id = 1, title = "spider man", hasFav = false),
                Movie(id = 2, title = "matrix", hasFav = false),
                Movie(id = 3, title = "avatar", hasFav = true),
                Movie(id = 4, title = "hulk", hasFav = false),

                )

val favsMockedList=
        listOf(
                Movie(id = 3, title = "avatar", hasFav = true),
                Movie(id = 5, title = "batman", hasFav = true),

                )

val moviesDbMockedList=
        listOf(
                Movie(id = 3, title = "avatar", hasFav = true),
                Movie(id = 5, title = "batman", hasFav = true),
                Movie(id = 1, title = "spider man", hasFav = false)

                )

val moviesMockResponse=MoviesModel(page = 1,results = moviesMockedList)
val emptyMoviesMockResponse=MoviesModel(page = 1,results = emptyList())

val reviewsMockedList=
        listOf(
               Review(id = "1000", movie_id = 2),
               Review(id = "1100", movie_id = 2),
                Review(id = "1300", movie_id = 2)
        )

val reviewsMockResponse=ReviewsModel(page = 1,results = reviewsMockedList)
val emptyReviewsMockResponse=MoviesModel(page = 1,results = emptyList())


fun errorResponseBody(): ResponseBody {

    /*return *//*"{\"key\":[\"somestuff\"]}"*/
    return "{\"errors\":[{\"key\":\"email\",\"value\":\"\u0627\u0644\u0628\u0631\u064a\u062f \u0627\u0644\u0625\u0644\u0643\u062a\u0631\u0648\u0646\u064a \u0645\u0633\u062c\u0644 \u0645\u0646 \u0642\u0628\u0644\"}]}"
        .toResponseBody("application/json".toMediaTypeOrNull())
}