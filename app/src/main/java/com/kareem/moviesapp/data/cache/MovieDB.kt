package com.kareem.moviesapp.data.cache
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [WeatherModel::class], version = 1, exportSchema = false)
@TypeConverters(WeatherTypeConverter::class, WeatherListTypeConverters::class)
abstract class MovieDB : RoomDatabase(){
    abstract fun weatherDao(): MovieDao

}
