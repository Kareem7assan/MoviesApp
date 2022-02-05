package com.kareem.moviesapp.data.repository

import com.kareem.moviesapp.data.cache.MovieDao
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.movies_model.MoviesModel
import com.kareem.moviesapp.data.model.rate.BaseRateModel
import com.kareem.moviesapp.data.model.reviews.Review
import com.kareem.moviesapp.data.model.reviews.ReviewsModel
import com.kareem.moviesapp.data.remote.MoviesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MoviesRepositoryImp @Inject constructor(
    private val api: MoviesApi,
    private val db: MovieDao
): MoviesRepository  {
    override suspend fun getMoviesCache(): List<Movie> {
        return db.getAllMovies()
    }

    override suspend fun getMovies(page:Int): Flow<Response<MoviesModel>> {
        return flow { emit(api.getLatestMovies(page = page)) }

    }

    override suspend fun saveMovies(movies: List<Movie>) {
        db.addMovies(movies = movies)
    }

    override suspend fun markAsFav(movie: Movie) {
        db.markAsFavourite(movie)
    }

    override suspend fun markAsUnFav(movie: Movie) {
        db.markAsUnFavourite(movie)
    }

    override suspend fun getReviews(movieId: Int,page: Int): Flow<Response<ReviewsModel>> {
        return flow { emit(api.getReviews(movieId,page)) }
    }

    override suspend fun saveReviews(reviews: List<Review>) {
        return db.addReviews(reviews)
    }

    override suspend fun getReviewsCache(movieId: Int): List<Review> {
        return db.getMovieReviews(movieId).first().reviews
    }

    override suspend fun addRate(movieId: Int, rate: String): Response<BaseRateModel> {
        return api.addRate(movieId, rate)
    }

}