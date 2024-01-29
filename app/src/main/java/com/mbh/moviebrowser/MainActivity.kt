package com.mbh.moviebrowser

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mbh.moviebrowser.features.movieDetails.MovieDetailsScreen
import com.mbh.moviebrowser.features.movieDetails.MovieDetailsViewModel
import com.mbh.moviebrowser.features.movieList.MovieListScreen
import com.mbh.moviebrowser.features.movieList.MovieListViewModel
import com.mbh.moviebrowser.store.MovieStore

class MainActivity : AppCompatActivity() {

    val movieStore = MovieStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        MovieListScreen(
                            viewModel = MovieListViewModel(movieStore),
                            onDetailsClicked = {
                                navController.navigate("details")
                            },
                        )
                    }
                    composable("details") {
                        MovieDetailsScreen(
                            viewModel = MovieDetailsViewModel(movieStore),
                        )
                    }
                }
            }
        }
    }
}
