package com.application.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.application.movieapp.di.AppContainer
import com.application.movieapp.ui.DetailScreen
import com.application.movieapp.ui.HomeScreen
import com.application.movieapp.viewmodel.MovieUiState
import com.application.movieapp.viewmodel.MovieViewModel

import com.application.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieViewModel(AppContainer.movieRepository) as T
            }
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val uiState by viewModel.uiState.collectAsState()
                    
                    SharedTransitionLayout {
                        NavHost(
                            navController = navController, 
                            startDestination = "home",
                            enterTransition = {
                                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                            },
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                            },
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                            },
                            popExitTransition = {
                                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                            }
                        ) {
                            composable("home") {
                                HomeScreen(
                                    viewModel = viewModel,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedContentScope = this@composable,
                                    onMovieClick = { movie ->
                                        navController.navigate("detail/${movie.id}")
                                    }
                                )
                            }
                            composable("detail/{movieId}") { backStackEntry ->
                                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                                if (uiState is MovieUiState.Success) {
                                    val movie = (uiState as MovieUiState.Success).movies.find { it.id == movieId }
                                    movie?.let {
                                        DetailScreen(
                                            movie = it,
                                            sharedTransitionScope = this@SharedTransitionLayout,
                                            animatedContentScope = this@composable,
                                            onBackClick = { navController.popBackStack() }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
