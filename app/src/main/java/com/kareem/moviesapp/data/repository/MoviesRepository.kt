package com.kareem.moviesapp.data.repository

import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.movies_model.MoviesModel
import com.kareem.moviesapp.data.model.rate.BaseRateModel
import com.kareem.moviesapp.data.model.reviews.Review
import com.kareem.moviesapp.data.model.reviews.ReviewsModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*

interface MoviesRepository {
    suspend fun getMoviesCache(): List<Movie>
    suspend fun getMovies(page:Int): Flow<Response<MoviesModel>>
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun markAsFav(movie: Movie)
    suspend fun markAsUnFav(movie: Movie)
    suspend fun getReviews(movieId: Int,page: Int):Flow<Response<ReviewsModel>>
    suspend fun getReviewsCache(movieId: Int):List<Review>
    suspend fun saveReviews(reviews: List<Review>)
    suspend fun addRate(movieId: Int,rate:String):Response<BaseRateModel>


}