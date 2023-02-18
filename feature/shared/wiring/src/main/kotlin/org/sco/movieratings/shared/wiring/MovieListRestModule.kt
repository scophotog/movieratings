package org.sco.movieratings.shared.wiring

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.shared.impl.data.remote.MovieDetailsRest
import org.sco.movieratings.shared.impl.data.remote.MovieListRest
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object MovieListRestModule {

    @Provides
    fun restList(retrofit: Retrofit): MovieListRest =
        retrofit.create()

    @Provides
    fun restDetails(retrofit: Retrofit): MovieDetailsRest =
        retrofit.create()
}