package org.sco.movieratings.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import org.sco.movieratings.BuildConfig
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ApiKeyModule {

    @Provides
    @Named("api_key")
    fun provideApiKey(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY)
                .build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }
}