package org.sco.movieratings.movielist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.movielist.data.remote.MovieListRest
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object MovieListRestModule {

    @Provides
    fun rest(retrofit: Retrofit): MovieListRest =
        retrofit.create()
}