package org.sco.movieratings.movielist.fake.di

import dagger.Binds
import dagger.Reusable
import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.fake.FakeMovieListInteractor

@dagger.Module
interface FakeMovieListFeatureModule {

    @Binds
    @Reusable
    fun interactor(impl: FakeMovieListInteractor): MovieListInteractor
}