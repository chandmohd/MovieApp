package com.application.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.movieapp.model.Movie
import com.application.movieapp.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading
            repository.getNowPlayingMovies()
                .onSuccess { movies ->
                    _uiState.value = MovieUiState.Success(movies)
                }
                .onFailure { error ->
                    _uiState.value = MovieUiState.Error(error.message ?: "An unexpected error occurred")
                }
        }
    }
}
