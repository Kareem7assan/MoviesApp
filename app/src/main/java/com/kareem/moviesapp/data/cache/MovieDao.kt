package com.kareem.moviesapp.data.cache
import androidx.room.*
import com.kareem.moviesapp.data.model.movies_model.Movie
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    /*order by id desc limit 1*/
    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies:List<Movie>)

    @Update
    suspend fun markAsFavourite(movies:Movie)

    @Update
    suspend fun markAsUnFavourite(movies:Movie)

}
