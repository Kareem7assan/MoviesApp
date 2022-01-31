package com.kareem.moviesapp.data.repository

import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.movies_model.MoviesModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MoviesRepository {
    suspend fun getMoviesCache(): List<Movie>
    suspend fun getMovies(page:Int): Flow<Response<MoviesModel>>
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun markAsFav(movie: Movie)
    suspend fun markAsUnFav(movie: Movie)

}