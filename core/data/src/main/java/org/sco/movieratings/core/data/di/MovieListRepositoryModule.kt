package org.sco.movieratings.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.core.data.repository.MovieListRepository
import org.sco.movieratings.core.data.repository.MovieListRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface MovieListRepositoryModule {
    @Binds
    fun bindMovieListRepository(
        repository: MovieListRepositoryImpl
    ): MovieListRepository
}