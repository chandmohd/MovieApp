package com.application.movieapp.di

import com.application.movieapp.network.MovieApiService
import com.application.movieapp.repository.MovieRepository

/**
 * A simple Dependency Injection container for the application.
 */
object AppContainer {
    private val apiService: MovieApiService by lazy {
        MovieApiService.create()
    }

    val movieRepository: MovieRepository by lazy {
        MovieRepository(apiService)
    }
}