package org.sco.movieratings.moviedetails.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.moviedetails.api.MovieDetailsRepository
import org.sco.movieratings.moviedetails.repository.MovieDetailsRepositoryImpl

@Module(
    includes = [
        FavoriteMovieDaoModule::class,
        MovieDetailRestModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
interface MovieDetailsDataModule {

    @Binds
    @Reusable
    fun repository(impl: MovieDetailsRepositoryImpl): MovieDetailsRepository
}