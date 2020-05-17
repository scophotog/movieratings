package org.sco.movieratings.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.sco.movieratings.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@Module
class ApiManager {

    @Provides
    fun provideMovieApi(): TheMovieDBService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(getGsonConverterFactory())
            .build()
            .create(TheMovieDBService::class.java)
    }

    @Provides
    fun provideMoviesService(): MoviesService {
        return MoviesService()
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

    private fun getGsonConverterFactory(): GsonConverterFactory {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return GsonConverterFactory.create(gsonBuilder.create())
    }

    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(addAPIKey())
            .addInterceptor(interceptor)
            .build()
    }

    companion object {
        private const val BASE_URL = "http://api.themoviedb.org/3/"
    }
}