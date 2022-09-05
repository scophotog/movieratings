package org.sco.movieratings.moviedetails.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import org.sco.movieratings.api.response.Review

@Composable
fun MovieReview(review: Review) {
    var isExpanded by remember { mutableStateOf(false) }
    var showReadMore by remember { mutableStateOf(false) }
    Card(elevation = 3.dp, modifier = Modifier.clickable(enabled = showReadMore) {
        isExpanded = !isExpanded
    }) {
        Column(Modifier.padding(top = 2.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)) {
            Text(text = review.author ?: "Unknown Author",
                style = MaterialTheme.typography.subtitle1)
            Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f), thickness = (0.5).dp)
            Text(
                text = review.content ?: "No Review Data",
                style = MaterialTheme.typography.body2,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    if (result.hasVisualOverflow) {
                        showReadMore = true
                    }
                }
            )
            if (showReadMore) {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                    Text(
                        text = if (isExpanded) { "Read Less" } else { "Read More" }
                    )
                    Icon(
                        painter = if (isExpanded) { painterResource(android.R.drawable.arrow_up_float) } else { painterResource(android.R.drawable.arrow_down_float) },
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
    MovieReview(review = Review(author = "Some Person", content = reviewContent))
}