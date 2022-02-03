package com.kareem.moviesapp.data.cache
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kareem.moviesapp.data.model.movies_model.Movie
import com.kareem.moviesapp.data.model.reviews.Review


@Database(entities = [Movie::class,Review::class], version = 1, exportSchema = false)
abstract class MovieDB : RoomDatabase(){
    abstract fun movieDao(): MovieDao

}
