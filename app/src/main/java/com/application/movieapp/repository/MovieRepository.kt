package com.application.movieapp.repository

import com.application.movieapp.model.Movie
import com.application.movieapp.network.MovieApiService

import retrofit2.HttpException
import java.io.IOException

class MovieRepository(private val apiService: MovieApiService) {
    suspend fun getNowPlayingMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getNowPlayingMovies(apiKey = "97df2f7609a8a2f39a25b33b41dc4208")
            Result.success(response.results)
        } catch (e: IOException) {
            Result.failure(Exception("Network error. Please check your internet connection."))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error (${e.code()}). Please try again later."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
