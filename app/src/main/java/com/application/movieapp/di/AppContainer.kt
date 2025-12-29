package com.application.movieapp.di

import android.content.Context
import androidx.room.Room
import com.application.movieapp.data.local.MovieDatabase
import com.application.movieapp.network.MovieApiService
import com.application.movieapp.repository.MovieRepository

/**
 * A simple Dependency Injection container for the application.
 */
object AppContainer {
    private lateinit var applicationContext: Context

    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    private val database: MovieDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movie_db"
        ).build()
    }

    private val apiService: MovieApiService by lazy {
        MovieApiService.create()
    }

    val movieRepository: MovieRepository by lazy {
        MovieRepository(apiService, database.movieDao())
    }
}