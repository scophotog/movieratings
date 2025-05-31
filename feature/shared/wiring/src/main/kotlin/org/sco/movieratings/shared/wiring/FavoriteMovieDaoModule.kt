package org.sco.movieratings.shared.wiring

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.db.MovieDatabase
import org.sco.movieratings.db.api.MovieDao

@Module
@InstallIn(SingletonComponent::class)
object FavoriteMovieDaoModule {

    @Provides
    @Reusable
    fun favoritesDao(db: MovieDatabase): MovieDao =
        db.movieDao()
}