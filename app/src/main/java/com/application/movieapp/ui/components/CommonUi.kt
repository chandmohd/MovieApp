package com.application.movieapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.application.movieapp.model.Movie

@Composable
fun HorizontalMovieGrid(
    movies: List<Movie>,
    bookmarkedMovieIds: Set<Int>,
    onMovieClick: (Movie) -> Unit,
    onBookmarkClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(1),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.height(200.dp)
    ) {
        items(movies) { movie ->
            MovieCard(
                movie = movie,
                isBookmarked = bookmarkedMovieIds.contains(movie.id),
                onClick = { onMovieClick(movie) },
                onBookmarkClick = { onBookmarkClick(movie) }
            )
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    isBookmarked: Boolean,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .width(140.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = movie.fullPosterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Bookmark Icon with blurry background
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
            ) {
                // Blurry background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(10.dp)
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                
//                IconButton(
//                    onClick = onBookmarkClick,
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    Icon(
//                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
//                        contentDescription = "Bookmark",
//                        tint = Color.White,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
            }
        }
    }
}

@Composable
fun CircularRatingIndicator(
    voteAverage: Double,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    strokeWidth: Dp = 4.dp,
    animationDuration: Int = 1000
) {
    var animationPlayed by rememberSaveable { mutableStateOf(false) }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) (voteAverage.toFloat() / 10f) else 0f,
        animationSpec = tween(
            durationMillis = animationDuration
        ),
        label = "rating_animation"
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        val primaryColor = MaterialTheme.colorScheme.primary
        val secondaryColor = MaterialTheme.colorScheme.primaryContainer

        Canvas(modifier = Modifier.size(size)) {
            drawArc(
                color = secondaryColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = primaryColor,
                startAngle = -90f,
                sweepAngle = 360 * curPercentage.value,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = String.format("%.1f", voteAverage),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = (size.value * 0.3).sp,
            fontWeight = FontWeight.Bold
        )
    }
}
