package org.sco.movieratings.movielist.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.MovieListViewModel

@Composable
fun MoviePosters(
    viewModel: MovieListViewModel,
    selectMovie: (Int) -> Unit
) {
//    val moviePosters = viewModel.viewState.collectAsState(initial = listOf<MovieSchema>())

    Scaffold(
        backgroundColor = MaterialTheme.colors.primarySurface
    ) {

    }
}