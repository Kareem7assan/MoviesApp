package com.kareem.moviesapp.data.cache
import androidx.room.*
import com.kareem.moviesapp.data.model.movies_model.Movie
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

}
