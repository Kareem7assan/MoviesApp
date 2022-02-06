package com.kareem.moviesapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.remote.NetWorkMovieState
import com.kareem.moviesapp.data.remote.RoomMoviesState
import com.kareem.moviesapp.domain.MoviesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class HomeViewModel @Inject constructor(private val moviesUseCase: MoviesUseCases) : ViewModel(){

    private val _moviesFlow= MutableStateFlow<NetWorkMovieState>(NetWorkMovieState.Loading)
    private val _favFlow= MutableStateFlow<RoomMoviesState>(RoomMoviesState.Loading)

    val moviesFlow= _moviesFlow.asStateFlow()
    val favFlow= _favFlow.asStateFlow()


    private val handler= CoroutineExceptionHandler { coroutineContext, throwable ->
        _moviesFlow.value=NetWorkMovieState.Error(throwable)
        _favFlow.value=RoomMoviesState.Empty

    }

    fun showHomeMovies(page: Int)  {
        viewModelScope.launch(Job() + handler){
                moviesUseCase.showAllMovies(page = page)
                        .onStart { _moviesFlow.value=NetWorkMovieState.Loading }
                        .onCompletion { _moviesFlow.value=NetWorkMovieState.StopLoading }
                        .collect { _moviesFlow.value=NetWorkMovieState.Success(it) }

        }
    }

    fun showMyFavouriteMovies()  {
        viewModelScope.launch(Job() + handler){
            moviesUseCase.showFavouriteMovies()
                .onStart { _favFlow.value=RoomMoviesState.Loading }
                .onCompletion { _favFlow.value=RoomMoviesState.StopLoading }
                .onEmpty { _favFlow.value=RoomMoviesState.Empty }
                .catch {  _favFlow.value=RoomMoviesState.Empty }
               .collect { if (it.isNotEmpty()) _favFlow.value=RoomMoviesState.Success(it) else throw IllegalStateException()}
        }
    }

    fun changeFavourite(movie: Movie){
        viewModelScope.launch(Job() + handler) {
            if (movie.hasFav == true) {
                moviesUseCase.markAsUnFavourite(movie)
            } else {
                moviesUseCase.markAsFavourite(movie)

            }
        }
    }



}