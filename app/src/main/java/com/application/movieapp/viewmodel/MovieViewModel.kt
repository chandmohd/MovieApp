package com.application.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.movieapp.model.Movie
import com.application.movieapp.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>, val bookmarkedMovies: List<Movie> = emptyList()) : MovieUiState()
    data class Error(val message: String, val bookmarkedMovies: List<Movie> = emptyList()) : MovieUiState()
}

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    init {
        observeBookmarks()
        fetchMovies()
    }

    private fun observeBookmarks() {
        viewModelScope.launch {
            repository.getBookmarkedMovies().collect { bookmarks ->
                val currentState = _uiState.value
                if (currentState is MovieUiState.Success) {
                    _uiState.value = currentState.copy(bookmarkedMovies = bookmarks)
                } else if (currentState is MovieUiState.Error) {
                    _uiState.value = currentState.copy(bookmarkedMovies = bookmarks)
                }
            }
        }
    }

    fun fetchMovies() {
        viewModelScope.launch {
            val bookmarks = if (_uiState.value is MovieUiState.Success) {
                (_uiState.value as MovieUiState.Success).bookmarkedMovies
            } else if (_uiState.value is MovieUiState.Error) {
                (_uiState.value as MovieUiState.Error).bookmarkedMovies
            } else {
                emptyList()
            }

            _uiState.value = MovieUiState.Loading
            repository.getNowPlayingMovies()
                .onSuccess { movies ->
                    _uiState.value = MovieUiState.Success(movies, bookmarks)
                }
                .onFailure { error ->
                    _uiState.value = MovieUiState.Error(error.message ?: "An unexpected error occurred", bookmarks)
                }
        }
    }

    fun toggleBookmark(movie: Movie) {
        viewModelScope.launch {
            val isBookmarked = repository.isBookmarked(movie.id).first()
            if (isBookmarked) {
                repository.unbookmarkMovie(movie)
            } else {
                repository.bookmarkMovie(movie)
            }
        }
    }

    fun isBookmarked(movieId: Int): Flow<Boolean> {
        return repository.isBookmarked(movieId)
    }
}
