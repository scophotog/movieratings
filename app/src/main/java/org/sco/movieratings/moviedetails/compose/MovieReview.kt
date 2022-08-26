package org.sco.movieratings.moviedetails.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import org.sco.movieratings.api.response.Review

@Composable
fun MovieReviewList(reviewList: List<Review>, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.wrapContentHeight()
    ) {
        Text(text = "Reviews", style = MaterialTheme.typography.h5)
        for(review in reviewList) {
            MovieReview(review)
        }
    }
}

@Composable
fun MovieReview(review: Review) {
    Card(elevation = 3.dp) {
        Column(Modifier.padding(horizontal = 4.dp)) {
            Text(text = review.author ?: "Unknown Author", style = MaterialTheme.typography.subtitle1)
            Divider(color = Color.Black.copy(alpha = 0.5f), thickness = (0.5).dp)
            Text(
                text = review.content ?: "No Review Data",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Preview
@Composable
fun MovieReviewPreview(@PreviewParameter(LoremIpsum::class) reviewContent: String) {
    MovieReview(review = Review(author = "Some Person", content = reviewContent))
}