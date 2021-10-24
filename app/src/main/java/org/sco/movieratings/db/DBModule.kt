package org.sco.movieratings.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        MovieDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideFavoriteMovieDao(db: MovieDatabase) = db.movieDao()
}