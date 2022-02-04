package com.kareem.moviesapp.data.cache
import androidx.room.*
import com.kareem.moviesapp.data.cache.relations.MovieWithReviews
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.reviews.Review
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies:List<Movie>)

    @Update
    suspend fun markAsFavourite(movie:Movie)

    @Update
    suspend fun markAsUnFavourite(movie:Movie)

    @Transaction
    @Query("SELECT * FROM movie WHERE id =:movieId ")
    suspend fun getMovieReviews(movieId:Int): List<MovieWithReviews>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReviews(reviews:List<Review>)


}
