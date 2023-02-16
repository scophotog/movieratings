package org.sco.movieratings.moviedetails.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.moviedetails.data.remote.MovieDetailsRest
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object MovieDetailRestModule {

    @Provides
    fun rest(retrofit: Retrofit): MovieDetailsRest =
        retrofit.create()
}