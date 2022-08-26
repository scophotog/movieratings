package org.sco.movieratings.moviedetails.compose

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sco.movieratings.R
import org.sco.movieratings.api.response.MoviePreview

@Composable
fun MoviePreviewList(moviePreviewList: List<MoviePreview>, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.wrapContentHeight()
    ) {
        val lineColor = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
        Text(
            text = "Previews",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.fillMaxWidth().drawBehind {
                val strokeWidth = 4
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = lineColor,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth.toFloat()
                )
            })
        for (preview in moviePreviewList) {
            val context = LocalContext.current
            MoviePreview(
                previewTitle = preview.name ?: continue,
                onRowClick = preview.key?.let { { startYouTube(it, context) } } ?: continue,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun startYouTube(moviePreviewKey: String, context: Context) {
    val youTubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$moviePreviewKey"))
    val youTubeWebIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$moviePreviewKey"))
    try {
        context.startActivity(youTubeIntent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(youTubeWebIntent)
    }
}

@Preview
@Composable
private fun MoviePreviewListPreview() {
    val movieList = mutableListOf<MoviePreview>()

    for (i in 1..5) {
        movieList.add(MoviePreview(key = "$i", name = "Preview $i"))
    }

    MoviePreviewList(moviePreviewList = movieList)
}


@Composable
fun MoviePreview(previewTitle: String, onRowClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = MaterialTheme.colors.background)
            .clickable(
                enabled = true,
                role = Role.Button,
                onClick = { onRowClick() }
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
            modifier = Modifier.background(color = Color.Red, shape = RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp)
        )
        Text(
            text = previewTitle,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Preview
@Composable
private fun MoviePreviewPreview() {
    MoviePreview(previewTitle = "Preview", onRowClick = { })
}
