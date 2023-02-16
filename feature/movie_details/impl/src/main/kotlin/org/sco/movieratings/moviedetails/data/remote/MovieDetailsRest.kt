package org.sco.movieratings.moviedetails.data.remote

import org.sco.movieratings.moviedetails.data.model.PreviewsResponse
import org.sco.movieratings.moviedetails.data.model.ReviewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface MovieDetailsRest {
    @GET("movie/{id}/videos")
    suspend fun getMoviePreviews(@Path("id") movieId: Int): Response<PreviewsResponse>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") movieId: Int): Response<ReviewsResponse>
}