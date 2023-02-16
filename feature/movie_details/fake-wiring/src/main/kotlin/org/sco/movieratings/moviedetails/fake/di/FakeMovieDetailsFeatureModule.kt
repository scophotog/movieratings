package org.sco.movieratings.moviedetails.fake.di

import dagger.Binds
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.moviedetails.fake.FakeMovieDetailsInteractor

@dagger.Module
@InstallIn(SingletonComponent::class)
interface FakeMovieDetailsFeatureModule {

    @Binds
    @Reusable
    fun interactor(impl: FakeMovieDetailsInteractor): MovieDetailInteractor
}