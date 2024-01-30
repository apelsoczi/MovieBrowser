package com.mbh.moviebrowser.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.mbh.moviebrowser.ui.navigation.MovieBrowserNavHost
import com.mbh.moviebrowser.ui.theme.MovieBrowserTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            MovieBrowserApp()
        }
    }

}

@Composable
fun MovieBrowserApp() {
    MovieBrowserTheme {
        val navController = rememberNavController()
        MovieBrowserNavHost(
            navController = navController,
        )
    }
}
