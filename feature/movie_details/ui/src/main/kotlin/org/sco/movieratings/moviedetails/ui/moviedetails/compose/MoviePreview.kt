package org.sco.movieratings.moviedetails.ui.moviedetails.compose

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sco.movieratings.moviedetails.api.MoviePreviewItem
import org.sco.movieratings.moviedetails.ui.moviedetails.R

@Composable
fun MoviePreview(moviePreview: MoviePreviewItem, modifier: Modifier = Modifier) {
    val previewTitle = moviePreview.name ?: return
    val moviePreviewKey = moviePreview.key ?: return
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = MaterialTheme.colors.background)
            .clickable(
                enabled = true,
                role = Role.Button,
                onClick = { startYouTube(moviePreviewKey, context) }
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                )
            )
            .padding(top = 4.dp, bottom = 4.dp, start = 4.dp)

    ) {
        Image(
            painter = painterResource(android.R.drawable.ic_media_play),
            contentDescription = stringResource(R.string.play_video),
            modifier = Modifier
                .background(color = Color.Red, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
        Text(
            text = previewTitle,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

private fun startYouTube(moviePreviewKey: String, context: Context) {
    val youTubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$moviePreviewKey"))
    val youTubeWebIntent =
        Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$moviePreviewKey"))
    try {
        context.startActivity(youTubeIntent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(youTubeWebIntent)
    }
}

@Preview
@Composable
private fun MoviePreviewPreview() {
    MoviePreview(moviePreview = MoviePreviewItem(name = "Title"))
}
