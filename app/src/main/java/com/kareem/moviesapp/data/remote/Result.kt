package com.kareem.moviesapp.data.remote

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    //data class Error(val exception: Exception) : Result<Nothing>()
    data class Error(val th: Throwable) : Result<Nothing>()
     class Loading<out T> : Result<T>()
     class StopLoading<out T> : Result<T>()


}
