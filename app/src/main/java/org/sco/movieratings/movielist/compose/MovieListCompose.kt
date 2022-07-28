package org.sco.movieratings.movielist

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import org.sco.movieratings.R

@Composable
fun MovieListLoadingSpinner() {
    CircularProgressIndicator(modifier = Modifier.testTag("progress"))
}

@Composable
fun NoResultsErrorText() {
    Text(stringResource(id = R.string.no_movies))
}
