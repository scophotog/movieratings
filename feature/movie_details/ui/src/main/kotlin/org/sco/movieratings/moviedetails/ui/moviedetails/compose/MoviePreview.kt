package org.sco.movieratings.moviedetails.ui.moviedetails.compose

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.core.net.toUri
import org.sco.movieratings.moviedetails.ui.moviedetails.R
import org.sco.movieratings.shared.api.MoviePreviewItem

@Composable
fun MoviePreview(moviePreview: MoviePreviewItem, modifier: Modifier = Modifier) {
    val previewTitle = moviePreview.name ?: return
    val moviePreviewKey = moviePreview.key ?: return
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .clickable(
                enabled = true,
                role = Role.Button,
                onClick = { startYouTube(moviePreviewKey, context) }
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
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
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

private fun startYouTube(moviePreviewKey: String, context: Context) {
    val youTubeIntent = Intent(Intent.ACTION_VIEW, "vnd.youtube:$moviePreviewKey".toUri())
    val youTubeWebIntent =
        Intent(Intent.ACTION_VIEW, "http://www.youtube.com/watch?v=$moviePreviewKey".toUri())
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
