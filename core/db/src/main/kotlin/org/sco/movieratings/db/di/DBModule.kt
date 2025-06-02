package org.sco.movieratings.db.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sco.movieratings.db.MovieDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DBModule {
    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ) : MovieDatabase = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        "movie-db"
    ).build()
}