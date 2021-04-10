package org.sco.movieratings.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.sco.movieratings.api.response.*
import java.lang.RuntimeException
import javax.inject.Inject

class MoviesService @Inject constructor(
    private val api: TheMovieDBService
) {

    suspend fun getMovieReviews(id: Int): Flow<Result<List<Review>>> {
        return flow {
            emit(Result.success(api.getMovieReviews(id).reviews!!))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    suspend fun getMoviePreviews(id: Int): Flow<Result<List<Preview>>> {
        return flow {
            emit(Result.success(api.getMoviePreviews(id).previews!!))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    suspend fun getTopRatedMovies(): Flow<Result<List<Movie>>> {
        return flow {
            emit(Result.success(api.getTopRatedMovies().movies!!))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    suspend fun getPopularMovies(): Flow<Result<List<Movie>>> {
        return flow {
            emit(Result.success(api.getPopularMovies().movies!!))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }
}