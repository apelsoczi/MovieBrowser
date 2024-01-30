package com.mbh.moviebrowser.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mbh.moviebrowser.features.details.DetailsScreen
import com.mbh.moviebrowser.features.trending.TrendingScreen

@Composable
fun MovieBrowserNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            TrendingScreen(
                onDetailsClicked = {
                    navController.navigate("details")
                },
            )
        }
        composable("details") {
            DetailsScreen()
        }
    }
}
