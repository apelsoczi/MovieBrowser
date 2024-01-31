package com.mbh.moviebrowser.features.details.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MovieHeroBanner(
    loading: Boolean,
    backdropUrl: String,
    coverUrl: String,
    title: String,
    year: String,
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
        ) {
            AsyncImage(
                model = backdropUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
            )
        }
        val captionString = buildAnnotatedString {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            append(title)
            pushStyle(SpanStyle(fontWeight = FontWeight.Light))
            append(" ($year)")
        }
        Text(
            text = captionString,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun MovieHeroBannerPreview() {
    MovieHeroBanner(
        loading = false,
        title = "Aquaman and the Lost Kingdom",
        coverUrl = "/7lTnXOy0iNtBAdRP3TZvaKJ77F6.jpg",
        backdropUrl = "/4gV6FOT4mEF4JaOmurO1kQSQ0Zl.jpg",
        year = "2023",
    )
}