package di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.sco.movieratings.db.MovieDao
import org.sco.movieratings.db.MovieDatabase

@Module
object FavoriteMovieDaoModule {

    @Provides
    @Reusable
    fun favoritesDao(db: MovieDatabase): MovieDao =
        db.movieDao()
}