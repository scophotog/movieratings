package di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import org.sco.movieratings.movielist.api.MovieListInteractor
import org.sco.movieratings.movielist.interactor.MovieListInteractorImpl

@Module
interface MovieListFeatureModule {

    @Binds
    @Reusable
    fun interactor(impl: MovieListInteractorImpl): MovieListInteractor
}