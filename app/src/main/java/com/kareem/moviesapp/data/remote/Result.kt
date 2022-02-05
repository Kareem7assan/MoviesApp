package com.kareem.moviesapp.data.remote

import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.reviews.Review

sealed class NetWorkMovieState {
    data class Success(val data: List<Movie>) : NetWorkMovieState()
    data class Error(val th: Throwable) : NetWorkMovieState()
    //object Idle : NetWorkState()
    object Loading : NetWorkMovieState()
    object StopLoading: NetWorkMovieState()
}

sealed class RoomMoviesState {
    data class Success(val data: List<Movie>) : RoomMoviesState()
    object Empty : RoomMoviesState()
    //object Idle : RoomState()
    object Loading : RoomMoviesState()
    object StopLoading: RoomMoviesState()
}

sealed class NetWorkReviewsState {
    data class Success(val data: List<Review>) : NetWorkReviewsState()
    data class Error(val th: Throwable) : NetWorkReviewsState()
    object Loading : NetWorkReviewsState()
    object StopLoading: NetWorkReviewsState()

}


sealed class RoomMovieState {
    data class Success(val data: Movie) : RoomMovieState()
    data class Error(val th: Throwable) : RoomMovieState()
    object Loading : RoomMovieState()
    object StopLoading: RoomMovieState()

}
