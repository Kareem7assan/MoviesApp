package com.kareem.moviesapp.presentation.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.remote.NetWorkRateState
import com.kareem.moviesapp.data.remote.NetWorkReviewsState
import com.kareem.moviesapp.data.remote.RoomMovieState
import com.kareem.moviesapp.data.remote.RoomMoviesState
import com.kareem.moviesapp.domain.DetailsUseCases
import com.kareem.moviesapp.domain.MoviesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class DetailsViewModel @Inject constructor(
        private val detailsUseCase: DetailsUseCases,
        private val moviesUseCase:MoviesUseCases
        ) : ViewModel(){

    private val _reviewsFlow= MutableStateFlow<NetWorkReviewsState>(NetWorkReviewsState.Loading)
    private val _rateFlow= MutableStateFlow<NetWorkRateState>(NetWorkRateState.StopLoading)
    private val _detailsFlow= MutableStateFlow<RoomMovieState>(RoomMovieState.Loading)

    val reviewsFlow= _reviewsFlow.asStateFlow()
    val rateFlow= _rateFlow.asStateFlow()
    val detailsFlow= _detailsFlow.asStateFlow()


    private val handler= CoroutineExceptionHandler { coroutineContext, throwable ->
        _reviewsFlow.value=NetWorkReviewsState.Error(throwable)
        _detailsFlow.value=RoomMovieState.Error(throwable)
        _rateFlow.value=NetWorkRateState.Error(throwable)

    }

    fun showMovieReviews(movieId:Int,page: Int)  {
        viewModelScope.launch(Job() + handler){
            detailsUseCase.getReviews(movieId = movieId,page = page)
                        .onStart { _reviewsFlow.value=NetWorkReviewsState.Loading }
                        .onCompletion { _reviewsFlow.value=NetWorkReviewsState.StopLoading }
                        .catch {  _reviewsFlow.value=NetWorkReviewsState.Error(it) }
                        .onEmpty { _reviewsFlow.value=NetWorkReviewsState.Empty }
                        .collect { _reviewsFlow.value=NetWorkReviewsState.Success(it) }

        }
    }

    fun showMovieDetails(movieId: Int)  {
        viewModelScope.launch(Job() + handler){
            detailsUseCase.getMovieDetails(movieId)
                    .onStart { _detailsFlow.value=RoomMovieState.Loading }
                    .onCompletion { _detailsFlow.value=RoomMovieState.StopLoading }
                    .catch {  _detailsFlow.value=RoomMovieState.Error(it) }
                    .collect {  _detailsFlow.value=RoomMovieState.Success(it) }
        }
    }

    fun addRate(movieId: Int,rate:String){
        viewModelScope.launch(Job() + handler){
            _rateFlow.value=NetWorkRateState.Loading
            _rateFlow.value=NetWorkRateState.Success(detailsUseCase.addRate(movieId, rate).body()!!)
            _rateFlow.value=NetWorkRateState.StopLoading


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