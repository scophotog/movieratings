package org.sco.movieratings.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
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
                .addQueryParameter("api_key", System.getProperty("API_KEY"))
                .build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }
}