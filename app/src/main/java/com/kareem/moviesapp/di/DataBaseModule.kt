package com.kareem.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.kareem.moviesapp.data.cache.MovieDao
import com.kareem.moviesapp.data.cache.MovieDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

        @Provides
        fun provideChannelDao(movieDB: MovieDB): MovieDao = movieDB.weatherDao()

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext appContext: Context): MovieDB {
            return Room.databaseBuilder(
                appContext,
                MovieDB::class.java,
                "movie_DB"
            ).build()
        }
}