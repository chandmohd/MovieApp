package com.application.movieapp.repository

import com.application.movieapp.data.local.MovieDao
import com.application.movieapp.data.local.MovieEntity
import com.application.movieapp.model.Movie
import com.application.movieapp.network.MovieApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import retrofit2.HttpException
import java.io.IOException

class MovieRepository(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) {
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

    fun getBookmarkedMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { entities ->
            entities.map { it.toMovie() }
        }
    }

    suspend fun bookmarkMovie(movie: Movie) {
        movieDao.insertMovie(movie.toEntity())
    }

    suspend fun unbookmarkMovie(movie: Movie) {
        movieDao.deleteMovie(movie.toEntity())
    }

    fun isBookmarked(movieId: Int): Flow<Boolean> {
        return movieDao.isBookmarked(movieId)
    }
}

fun MovieEntity.toMovie() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun Movie.toEntity() = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount
)
