package com.mbh.moviebrowser.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mbh.moviebrowser.features.destinations.DetailsScreenDestination
import com.mbh.moviebrowser.features.destinations.TrendingScreenDestination
import com.mbh.moviebrowser.features.details.DetailsScreen
import com.mbh.moviebrowser.features.trending.TrendingScreen
import com.ramcosta.composedestinations.utils.composable

@Composable
fun MovieBrowserNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = TrendingScreenDestination.route
    ) {
        composable(TrendingScreenDestination) {
            TrendingScreen(
                navController = destinationsNavigator(navController)
            )
        }
        composable(DetailsScreenDestination) {
            DetailsScreen()
        }
    }
}
