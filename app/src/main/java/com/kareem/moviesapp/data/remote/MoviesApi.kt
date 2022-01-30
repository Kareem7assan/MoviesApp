package com.kareem.moviesapp.data.remote

import com.kareem.moviesapp.data.model.movies_model.MoviesModel
import com.kareem.moviesapp.data.remote.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("now_playing")
    suspend fun getLatestMovies(
        @Query("page") page:Int=1,
        @Query("language") language:String="en-US",
        @Query("api_key") apiKey:String="e7c8c79bac155e8f4a21476bfe58c90e"
    ): Response<MoviesModel>


}