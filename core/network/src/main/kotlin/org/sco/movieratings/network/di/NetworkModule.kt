package org.sco.movieratings.network.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sco.movieratings.network.TheMovieDbNetworkDataSource
import org.sco.movieratings.network.retrofit.RetrofitTheMovieDbNetwork
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun okHttpCallFactory(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("api_key") apiKey: Interceptor? = null
    ): Call.Factory {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(loggingInterceptor)
        if (apiKey != null) {
            builder.addInterceptor(apiKey)
        }
        return builder.build()
    }

}

@Module
@InstallIn(SingletonComponent::class)
internal interface TheMovieDbNetworkModule {
    @Binds
    fun binds(impl: RetrofitTheMovieDbNetwork): TheMovieDbNetworkDataSource
}