package org.sco.movieratings.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImagePathUrlModule {

    @Provides
    @Singleton
    @Named("image_path")
    fun imagePath() = "https://image.tmdb.org/t/p/w500"
}