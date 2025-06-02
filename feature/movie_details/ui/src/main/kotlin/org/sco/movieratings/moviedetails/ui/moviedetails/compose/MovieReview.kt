package org.sco.movieratings.moviedetails.ui.moviedetails.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import org.sco.movieratings.core.model.data.MovieReviewItem

@Composable
fun MovieReview(review: MovieReviewItem) {
    var isExpanded by remember { mutableStateOf(false) }
    var showReadMore by remember { mutableStateOf(false) }
    Card(modifier = Modifier.clickable(enabled = showReadMore) {
        isExpanded = !isExpanded
    }) {
        Column(Modifier.padding(top = 2.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)) {
            Text(
                text = review.author ?: "Unknown Author",
                style = MaterialTheme.typography.headlineSmall
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), thickness = (0.5).dp)
            Text(
                text = review.content ?: "No Review Data",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    if (result.hasVisualOverflow) {
                        showReadMore = true
                    }
                }
            )
            if (showReadMore) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Text(
                        text = if (isExpanded) {
                            "Read Less"
                        } else {
                            "Read More"
                        }
                    )
                    Icon(
                        painter = if (isExpanded) {
                            painterResource(android.R.drawable.arrow_up_float)
                        } else {
                            painterResource(android.R.drawable.arrow_down_float)
                        },
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieReviewPreview(@PreviewParameter(LoremIpsum::class) reviewContent: String) {
    MovieReview(
        review = MovieReviewItem(
            author = "Some Person",
            content = reviewContent
        )
    )
}