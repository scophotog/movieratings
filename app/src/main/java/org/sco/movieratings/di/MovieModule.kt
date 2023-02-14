package org.sco.movieratings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.sco.movieratings.repository.IMovieRepository
import org.sco.movieratings.repository.MovieRepository

@InstallIn(ViewModelComponent::class)
@Module
interface MovieModule {
    @Binds
    fun bindMovieRepository(movieRepository: MovieRepository): IMovieRepository
}