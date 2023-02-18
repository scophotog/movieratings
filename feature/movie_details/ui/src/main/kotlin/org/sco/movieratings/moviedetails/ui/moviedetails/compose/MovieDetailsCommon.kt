package org.sco.movieratings.moviedetails.ui.moviedetails.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset

@Composable
fun HeaderSection(text: String) {
    val lineColor = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
    Text(
        text = text,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 4
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = lineColor,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth.toFloat()
                )
            }
    )
}