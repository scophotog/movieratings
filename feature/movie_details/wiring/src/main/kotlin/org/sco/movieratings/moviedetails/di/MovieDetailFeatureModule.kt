package org.sco.movieratings.moviedetails.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import org.sco.movieratings.moviedetails.api.MovieDetailInteractor
import org.sco.movieratings.moviedetails.interactor.MovieDetailsInteractorImpl

@Module
interface MovieDetailFeatureModule {

    @Binds
    @Reusable
    fun interactor(impl: MovieDetailsInteractorImpl): MovieDetailInteractor
}