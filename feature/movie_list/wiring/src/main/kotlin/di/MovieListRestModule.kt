package di

import dagger.Module
import dagger.Provides
import org.sco.movieratings.movielist.data.remote.MovieListRest
import retrofit2.Retrofit
import retrofit2.create

@Module
object MovieListRestModule {

    @Provides
    fun rest(retrofit: Retrofit): MovieListRest =
        retrofit.create()
}