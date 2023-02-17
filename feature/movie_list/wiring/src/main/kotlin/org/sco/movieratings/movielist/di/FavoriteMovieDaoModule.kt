package org.sco.movieratings.movielist.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.db.MovieDatabase
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object FavoriteMovieDaoModule {

    @Provides
    @Reusable
    @Named("movie_list")
    fun favoritesDao(db: MovieDatabase): MovieDao =
        db.movieDao()
}