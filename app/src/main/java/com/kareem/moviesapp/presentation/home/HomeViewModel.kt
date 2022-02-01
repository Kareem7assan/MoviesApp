package com.kareem.moviesapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.remote.NetWorkState
import com.kareem.moviesapp.data.remote.RoomState
import com.kareem.moviesapp.domain.MoviesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class HomeViewModel @Inject constructor(private val moviesUseCase:MoviesUseCases) : ViewModel(){

    private val _moviesFlow= MutableStateFlow<NetWorkState>(NetWorkState.Idle)
    private val _favFlow= MutableStateFlow<RoomState>(RoomState.Idle)

    val moviesFlow= _moviesFlow.asStateFlow()
    val favFlow= _favFlow.asStateFlow()


    private val handler= CoroutineExceptionHandler { coroutineContext, throwable ->
        _moviesFlow.value=NetWorkState.Error(throwable)
        _favFlow.value=RoomState.Empty

    }

    fun showHomeMovies(page: Int)  {
        viewModelScope.launch(Job()+handler){
            moviesUseCase.showAllMovies(page = page)
                .onStart { _moviesFlow.value=NetWorkState.Loading }
                .onCompletion { _moviesFlow.value=NetWorkState.StopLoading }
               .collect { _moviesFlow.value=NetWorkState.Success(it) }
        }
    }

    fun showMyFavouriteMovies()  {
        viewModelScope.launch(Job()+handler){
            moviesUseCase.showFavouriteMovies()
                .onStart { _favFlow.value=RoomState.Loading }
                .onCompletion { _favFlow.value=RoomState.StopLoading }
                .onEmpty { _favFlow.value=RoomState.Empty }
                .catch {  _favFlow.value=RoomState.Empty }
               .collect { if (it.isNotEmpty()) _favFlow.value=RoomState.Success(it) else throw IllegalStateException()}
        }
    }

    fun changeFavourite(movie:Movie){
        viewModelScope.launch {
            if (movie.hasFav == true) {
                moviesUseCase.markAsUnFavourite(movie)
            } else {
                moviesUseCase.markAsFavourite(movie)

            }
        }
    }

}