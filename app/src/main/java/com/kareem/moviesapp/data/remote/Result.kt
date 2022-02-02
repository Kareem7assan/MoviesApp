package com.kareem.moviesapp.data.remote

import com.kareem.moviesapp.data.model.movies_model.Movie

sealed class NetWorkState {
    data class Success(val data: List<Movie>) : NetWorkState()
    data class Error(val th: Throwable) : NetWorkState()
    //object Idle : NetWorkState()
    object Loading : NetWorkState()
    object StopLoading: NetWorkState()


}

sealed class RoomState {
    data class Success(val data: List<Movie>) : RoomState()
    object Empty : RoomState()
    //object Idle : RoomState()
    object Loading : RoomState()
    object StopLoading: RoomState()


}
