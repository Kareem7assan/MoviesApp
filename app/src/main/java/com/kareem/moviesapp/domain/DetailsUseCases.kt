package com.kareem.moviesapp.domain

import android.util.Log
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.rate.BaseRateModel
import com.kareem.moviesapp.data.model.reviews.Review
import com.kareem.moviesapp.data.repository.MoviesRepository
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

class DetailsUseCases @Inject constructor(private val repository: MoviesRepository) {


  suspend fun getMovieDetails(movieId:Int) : Flow<Movie> = flow { emit(repository.getMoviesCache().find { it.id==movieId }!!) }


  suspend fun getReviews(movieId:Int,page: Int) : Flow<List<Review>> {
    return repository.getReviews(movieId, page)
            .transform {if (it.isSuccessful && it.body()?.results?.isNullOrEmpty()?.not()==true) emit(it.body()?.results!!) }
            .onEach { repository.saveReviews(it) }
            .onEmpty {if (repository.getReviewsCache(movieId).isNotEmpty()) emit(repository.getReviewsCache(movieId)) }


  }


  suspend fun addRate(movieId: Int,rate:String) = repository.addRate(movieId, rate)




}