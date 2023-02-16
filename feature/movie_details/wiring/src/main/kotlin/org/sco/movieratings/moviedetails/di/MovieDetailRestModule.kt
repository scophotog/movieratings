package org.sco.movieratings.moviedetails.di

import dagger.Module
import dagger.Provides
import org.sco.movieratings.moviedetails.data.remote.MovieDetailsRest
import retrofit2.Retrofit
import retrofit2.create

@Module
object MovieDetailRestModule {

    @Provides
    fun rest(retrofit: Retrofit): MovieDetailsRest =
        retrofit.create()
}