package com.mbh.moviebrowser.ui.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mbh.moviebrowser.ui.shimmer.shimmerBrush

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(shimmerBrush()),
        content = {}
    )
}

@Preview
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}