package org.sco.movieratings.moviedetails.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.moviedetails.interactor.MovieDetailsInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface MovieDetailFeatureModule {

    @Binds
    @Reusable
    fun interactor(impl: MovieDetailsInteractorImpl): MovieDetailInteractor
}