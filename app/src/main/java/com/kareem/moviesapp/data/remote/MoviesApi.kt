package com.kareem.moviesapp.data.remote

import com.kareem.moviesapp.data.model.movies_model.MoviesModel
import com.kareem.moviesapp.data.model.rate.BaseRateModel
import com.kareem.moviesapp.data.model.reviews.ReviewsModel
import retrofit2.Response
import retrofit2.http.*

interface MoviesApi {
    @GET("now_playing")
    suspend fun getLatestMovies(
        @Query("page") page:Int=1,
        @Query("language") language:String="en-US",
        @Query("api_key") apiKey:String="e7c8c79bac155e8f4a21476bfe58c90e"
    ): Response<MoviesModel>

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(
        @Path("movie_id") movieId:Int,
        @Query("page") page:Int=1,
        @Query("language") language:String="en-US",
        @Query("api_key") apiKey:String="e7c8c79bac155e8f4a21476bfe58c90e"
    ): Response<ReviewsModel>


    @FormUrlEncoded
    @POST("movie/{movie_id}/rating")
    suspend fun addRate(
        @Path("movie_id") movieId:Int,
        @Field("value") rate:String,
        @Query("language") language:String="en-US",
        @Query("api_key") apiKey:String="e7c8c79bac155e8f4a21476bfe58c90e",
        @Query("session_id") session_id:String="365b62d197573b400acfa8ffbe10ad7ce47640c7",
    ): Response<BaseRateModel>





}