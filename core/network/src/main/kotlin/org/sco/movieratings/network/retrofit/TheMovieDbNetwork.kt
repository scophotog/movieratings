package org.sco.movieratings.network.retrofit

import android.util.Log
import okhttp3.Call
import org.sco.movieratings.network.TheMovieDbNetworkDataSource
import org.sco.movieratings.network.model.Movie
import org.sco.movieratings.network.model.MoviePreview
import org.sco.movieratings.network.model.MoviesResponse
import org.sco.movieratings.network.model.PreviewsResponse
import org.sco.movieratings.network.model.Review
import org.sco.movieratings.network.model.ReviewsResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private interface TheMovieDbNetworkApi {
    @GET("movie/{id}/videos")
    suspend fun getMoviePreviews(@Path("id") movieId: Int): Response<PreviewsResponse>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") movieId: Int): Response<ReviewsResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): Response<MoviesResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<MoviesResponse>
}

@Singleton
internal class RetrofitTheMovieDbNetwork @Inject constructor(
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : TheMovieDbNetworkDataSource {
    private val networkApi =
        Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(TheMovieDbNetworkApi::class.java)

    override suspend fun getPopularMovies(): List<Movie> =
        apiCall { networkApi.getPopularMovies() }.getOrNull()?.movies ?: emptyList()

    override suspend fun getTopRatedMovies(): List<Movie> =
        apiCall { networkApi.getTopRatedMovies() }.getOrNull()?.movies ?: emptyList()

    override suspend fun getMovieReviews(movieId: Int): List<Review> =
        apiCall { networkApi.getMovieReviews(movieId) }.getOrNull()?.reviews ?: emptyList()


    override suspend fun getMoviePreviews(movieId: Int): List<MoviePreview> =
        apiCall { networkApi.getMoviePreviews(movieId) }.getOrNull()?.moviePreviews ?: emptyList()
}

suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
    val response: Response<T>
    try {
        response = call.invoke()
    } catch (t: Throwable) {
        Log.e("apiCall", "Api Error", t)
        return Result.failure(t)
    }

    return if (!response.isSuccessful) {
        val errorBody = response.errorBody()
        Result.failure(IOException("Bad Response: ${response.message()} ${response.code()} ${errorBody?.string()}"))
    } else {
        val responseBody = response.body()
        return if (responseBody == null) {
            Result.failure(IllegalStateException("response.body() was null"))
        } else {
            Result.success(responseBody)
        }
    }
}