package org.sco.movieratings.shared.impl.data.mapper

import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.shared.api.MovieListItem
import org.sco.movieratings.shared.api.MoviePreviewItem
import org.sco.movieratings.shared.api.MovieReviewItem
import org.sco.movieratings.shared.impl.data.model.Movie
import org.sco.movieratings.shared.impl.data.model.MoviePreview
import org.sco.movieratings.shared.impl.data.model.Review
import javax.inject.Inject
import javax.inject.Named

class MovieMapper @Inject constructor(
    @Named("image_path") private val imagePath: String,
    @Named("backdrop_path") private val backdropPath: String
) {
    fun mapMovieList(movieRawList: List<Movie>): List<MovieListItem> {
        return movieRawList.map {
            MovieListItem(
                id = it.id ?: -1,
                title = it.title ?: "",
                posterPath = it.posterPath?.run { imagePath + this } ?: "",
                overview = it.overview ?: "",
                releaseDate = it.releaseDate ?: "",
                popularity = it.popularity ?: -1.00,
                voteAverage = it.voteAverage ?: -1.00,
                backdropPath = it.backdropPath?.run { backdropPath + this } ?: ""
            )
        }
    }

    fun mapReviewList(movieReviewListRaw: List<Review>): List<MovieReviewItem> {
        return movieReviewListRaw.map { review ->
            MovieReviewItem(
                id = review.id,
                author = review.author,
                url = review.url,
                content = review.content
            )
        }
    }

    fun mapPreviewList(moviePreviewListRaw: List<MoviePreview>): List<MoviePreviewItem> {
        return moviePreviewListRaw.map { preview ->
            MoviePreviewItem(
                id = preview.id,
                key = preview.key,
                name = preview.name,
                site = preview.site,
                size = preview.size,
                type = preview.type,
                iso_3166_1 = preview.iso_3166_1,
                iso_6391 = preview.iso_6391
            )
        }
    }

    fun mapMovieSchemaToMovieList(movieRawList: List<MovieSchema>): List<MovieListItem> {
        return movieRawList.map {
            MovieListItem(
                id = it.id,
                title = it.title,
                posterPath = it.posterPath.run { imagePath + this },
            )
        }
    }
    
    fun mapToMovieSchema(movieListItem: MovieListItem): MovieSchema {
        return MovieSchema(
            id = movieListItem.id,
            title = movieListItem.title,
            posterPath = movieListItem.posterPath ?: "",
            overview = movieListItem.overview ?: "",
            releaseDate = movieListItem.releaseDate ?: "",
            popularity = movieListItem.popularity ?: 0.0,
            voteAverage = movieListItem.voteAverage?.toFloat() ?: 0.0f,
            backdropPath = movieListItem.backdropPath ?: ""
        )
    }
}
