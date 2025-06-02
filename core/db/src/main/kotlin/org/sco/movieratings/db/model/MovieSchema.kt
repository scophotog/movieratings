package org.sco.movieratings.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.sco.movieratings.core.model.data.MovieListItem

@Entity(
    tableName = "movies"
)
@Parcelize
data class MovieSchema(
    @PrimaryKey var id: Int,
    var title: String,
    var posterPath: String,
    var overview: String,
    var releaseDate: String,
    var popularity: Double,
    var voteAverage: Float,
    var backdropPath: String? = null
) : Parcelable {

    companion object {
        fun mock() = MovieSchema(
            id = 0,
            title = "Fake Movie",
            posterPath = "https://raw.githubusercontent.com/scophotog/movieratings/master/app/src/main/res/drawable/movie_poster.jpg",
            overview = "Fake overview",
            releaseDate = "01/01/2022",
            popularity = 10.0,
            voteAverage = 5.0f,
            backdropPath = null
        )
    }
}

fun MovieSchema.toMovieListItem() = MovieListItem(
    id = id,
    title = title,
    posterPath = posterPath,
)
