package com.kareem.moviesapp.data.repository

import com.kareem.moviesapp.data.model.movies_model.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MoviesRepository {
    suspend fun getMoviesCache(): Flow<List<Movie>>
    suspend fun getMovies(): Flow<Response<List<Movie>>>
    suspend fun saveMovies(movies: Flow<List<Movie>>)
    suspend fun markAsFav(movies: Movie)
    suspend fun markAsUnFav(movies: Movie)

}