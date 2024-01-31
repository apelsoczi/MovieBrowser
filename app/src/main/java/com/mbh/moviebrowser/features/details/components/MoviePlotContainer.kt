package com.mbh.moviebrowser.features.details.components

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mbh.moviebrowser.features.details.Actions

@Composable
fun MoviePlotContainer(
    loading: Boolean,
    favorite: Boolean,
    tagline: String,
    overview: String,
    handle: (Actions) -> Unit,
) {
    val floatingActionButtonHeight = 56.dp
    Box(
        Modifier.padding(18.dp)
    ) {
        // favorite button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .zIndex(1.0F),
            horizontalArrangement = Arrangement.Center,
        ) {
            val image = if (favorite) {
                painterResource(id = R.drawable.btn_star_big_on)
            } else {
                painterResource(id = R.drawable.btn_star_big_off)
            }
            val shape = if (favorite) FloatingActionButtonDefaults.shape else CircleShape
            FloatingActionButton(
                onClick = { handle(Actions.Favorite) },
                shape = shape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                ),
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = shape,
                ),
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                )
            }
        }

        // movie details container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = floatingActionButtonHeight / 2)
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(8.dp),
                )
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 16.dp + floatingActionButtonHeight / 2,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                )
            ) {
                tagline.takeIf { it.isNotEmpty() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                }
                overview.takeIf { it.isNotEmpty() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailedMovieContainterPreview() {
    MoviePlotContainer(
        loading = false,
        tagline = "The tide is turning.",
        overview = "Black Manta, still driven by the need to avenge his father's death and wielding the power of the mythic Black Trident, will stop at nothing to take Aquaman down once and for all. To defeat him, Aquaman must turn to his imprisoned brother Orm, the former King of Atlantis, to forge an unlikely alliance in order to save the world from irreversible destruction.",
        favorite = true,
        handle = {},
    )
}