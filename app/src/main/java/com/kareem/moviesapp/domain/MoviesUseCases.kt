package com.kareem.moviesapp.domain

import android.util.Log
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.repository.MoviesRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MoviesUseCases @Inject constructor(private val repository: MoviesRepository) {

  suspend fun showAllMovies(page:Int): Flow<List<Movie>> {
    return repository.getMovies(page)
            .transform {if (it.isSuccessful && it.body()?.results?.isNullOrEmpty()?.not()==true) emit(handleWithFavsItems(it.body()?.results!!)) }
            .onEach { repository.saveMovies(it) }
            .onEmpty { emit(repository.getMoviesCache()) }
            .catch { emit(repository.getMoviesCache()) }
  }

  suspend fun handleWithFavsItems(apiMovies: List<Movie>): List<Movie> = apiMovies.map {item-> item.copy(hasFav = isItemExistInFavDB(item)) }

  suspend fun showFavouriteMovies(): Flow<List<Movie>> {
    return flow { emit(repository.getMoviesCache()
                                .filter {it.hasFav==true}
    )
    }
  }

  suspend fun isItemExistInFavDB(movie: Movie): Boolean {
    repository.getMoviesCache().find { it.id==movie.id }!=null
    val itemCached=repository.getMoviesCache().find { it.id==movie.id }
    return itemCached!=null && itemCached.hasFav==true
  }



  suspend fun markAsFavourite(movie:Movie)  = repository.markAsFav(movie.copy(hasFav = true))

  suspend fun markAsUnFavourite(movie:Movie)  = repository.markAsUnFav(movie.copy(hasFav = false))









}