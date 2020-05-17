package org.sco.movieratings.api

import dagger.Component
import org.sco.movieratings.db.DBModule
import org.sco.movieratings.viewModel.MovieListViewModel

@Component(modules = [ApiManager::class, DBModule::class])
interface ApiComponent {

    fun inject(service: MoviesService)

    fun inject(viewModel: MovieListViewModel)

    fun inject(service: DBService)

}