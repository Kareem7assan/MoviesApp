package com.kareem.moviesapp.data.repository

import com.kareem.moviesapp.data.cache.MovieDao
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.movies_model.MoviesModel
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

}