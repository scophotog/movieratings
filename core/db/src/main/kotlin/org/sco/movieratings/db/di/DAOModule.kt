package org.sco.movieratings.db.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.db.MovieDatabase
import org.sco.movieratings.db.dao.MovieDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DAOModule {

    @Singleton
    @Provides
    fun provideMovieDao(
        database: MovieDatabase
    ): MovieDao = database.movieDao()

}