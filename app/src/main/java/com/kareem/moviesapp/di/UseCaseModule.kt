package com.kareem.moviesapp.di

import com.kareem.moviesapp.data.repository.MoviesRepository
import com.kareem.moviesapp.domain.MoviesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun providesMoviesUseCase(moviesRepository: MoviesRepository): MoviesUseCases {
        return MoviesUseCases(moviesRepository)
    }

}

