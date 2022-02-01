package com.kareem.moviesapp.di


import com.kareem.moviesapp.data.repository.MoviesRepository
import com.kareem.moviesapp.data.repository.MoviesRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MoviesRepositoryModule {
    @Binds
    abstract fun providesTestRepo(moviesRepository: MoviesRepositoryImp): MoviesRepository

}