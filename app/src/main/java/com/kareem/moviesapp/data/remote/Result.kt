package com.kareem.moviesapp.data.remote

sealed class NetWorkState {
    data class Success<out T>(val data: T) : NetWorkState()
    data class Error(val th: Throwable) : NetWorkState()
    object Idle : NetWorkState()
    object Loading : NetWorkState()
    object StopLoading: NetWorkState()


}

sealed class RoomState {
    data class Success<out T>(val data: T) : RoomState()
    data class Error(val th: Throwable) : RoomState()
    object Empty : RoomState()
    object Idle : RoomState()
    object Loading : RoomState()
    object StopLoading: RoomState()


}
