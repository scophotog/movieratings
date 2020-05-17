package org.sco.movieratings.db

import dagger.Module
import dagger.Provides
import org.sco.movieratings.api.DBService

@Module
class DBModule {

    @Provides
    fun provideDB(): DBService {
        return DBService()
    }
}