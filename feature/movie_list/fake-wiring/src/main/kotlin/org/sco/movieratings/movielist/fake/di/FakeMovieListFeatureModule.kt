package org.sco.movieratings.movielist.fake.di

import dagger.Binds
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.fake.FakeMovieListInteractor

@dagger.Module
@InstallIn(SingletonComponent::class)
interface FakeMovieListFeatureModule {

    @Binds
    @Reusable
    fun interactor(impl: FakeMovieListInteractor): MovieListInteractor
}