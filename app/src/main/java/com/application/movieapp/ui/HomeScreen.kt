package com.application.movieapp.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.application.movieapp.model.Movie
import com.application.movieapp.viewmodel.MovieUiState
import com.application.movieapp.viewmodel.MovieViewModel

import androidx.compose.ui.res.stringResource
import com.application.movieapp.R
import com.application.movieapp.ui.components.CircularRatingIndicator

@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerColors = if (isSystemInDarkTheme()) {
        listOf(
            Color.DarkGray.copy(alpha = 0.6f),
            Color.DarkGray.copy(alpha = 0.2f),
            Color.DarkGray.copy(alpha = 0.6f),
        )
    } else {
        listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )
    }

    return this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnim, y = translateAnim)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    viewModel: MovieViewModel, 
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onMovieClick: (Movie) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.now_playing_movies), fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is MovieUiState.Loading -> {
                    ShimmerMovieGrid()
                }
                is MovieUiState.Success -> {
                    if (state.movies.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_movies_found),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        MovieGrid(
                            movies = state.movies, 
                            sharedTransitionScope = sharedTransitionScope,
                            animatedContentScope = animatedContentScope,
                            onMovieClick = onMovieClick
                        )
                    }
                }
                is MovieUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.fetchMovies() }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerMovieGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false
    ) {
        items(10) {
            ShimmerMovieItem()
        }
    }
}

@Composable
fun ShimmerMovieItem() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .shimmerEffect()
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .shimmerEffect()
                )
                
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(14.dp)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(18.dp)
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieGrid(
    movies: List<Movie>, 
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onMovieClick: (Movie) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(
                movie = movie, 
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onMovieClick = onMovieClick
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieItem(
    movie: Movie, 
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onMovieClick: (Movie) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        onClick = { onMovieClick(movie) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp,
            hoveredElevation = 8.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            with(sharedTransitionScope) {
                AsyncImage(
                    model = movie.fullPosterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .sharedElement(
                            rememberSharedContentState(key = "image-${movie.id}"),
                            animatedVisibilityScope = animatedContentScope
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    CircularRatingIndicator(
                        voteAverage = movie.voteAverage,
                        size = 32.dp,
                        strokeWidth = 3.dp
                    )
                }
                
                Column {
                    Text(
                        text = stringResource(R.string.release_date_label, movie.releaseDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
