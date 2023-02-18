package org.sco.movieratings.shared.wiring

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.shared.api.MovieListRepository
import org.sco.movieratings.shared.impl.repository.MovieListRepositoryImpl

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