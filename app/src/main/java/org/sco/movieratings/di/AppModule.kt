package org.sco.movieratings.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.movielist.MovieListAdapter
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMovieListAdapter(@Named("image_path") imagePath: String) = MovieListAdapter(imagePath)
}