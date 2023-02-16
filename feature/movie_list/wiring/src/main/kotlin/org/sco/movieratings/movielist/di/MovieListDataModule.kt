package org.sco.movieratings.movielist.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.movielist.api.MovieListRepository
import org.sco.movieratings.movielist.repository.MovieListRepositoryImpl

@Module(
    includes = [
        FavoriteMovieDaoModule::class,
        MovieListRestModule::class
    ]
)
@InstallIn(SingletonComponent::class)
interface MovieListDataModule {

    @Binds
    @Reusable
    fun repository(impl: MovieListRepositoryImpl): MovieListRepository
}