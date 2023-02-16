package org.sco.movieratings.movielist.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.interactor.MovieListInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface MovieListFeatureModule {

    @Binds
    @Reusable
    fun interactor(impl: MovieListInteractorImpl): MovieListInteractor
}