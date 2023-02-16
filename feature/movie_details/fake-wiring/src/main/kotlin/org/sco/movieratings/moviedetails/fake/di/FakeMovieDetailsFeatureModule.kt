package org.sco.movieratings.moviedetails.fake.di

import dagger.Binds
import dagger.Reusable
import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.moviedetails.fake.FakeMovieDetailsInteractor

@dagger.Module
interface FakeMovieDetailsFeatureModule {

    @Binds
    @Reusable
    fun interactor(impl: FakeMovieDetailsInteractor): MovieDetailInteractor
}