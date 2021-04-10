package org.sco.movieratings.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.sco.movieratings.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

@Module
@InstallIn(FragmentComponent::class)
class MovieDBModule {

    @Provides
    fun provideMovieDBAPI(retrofit: Retrofit) : TheMovieDBService =
        retrofit.create(TheMovieDBService::class.java)

    @Provides
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getClient())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(addAPIKey())
            .addInterceptor(interceptor)
            .build()
    }
    private fun addAPIKey(): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                val url = request.url.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY)
                    .build()
                request = request.newBuilder().url(url).build()
                return chain.proceed(request)
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}