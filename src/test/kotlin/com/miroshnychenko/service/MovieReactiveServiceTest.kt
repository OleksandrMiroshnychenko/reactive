package com.miroshnychenko.service

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MovieReactiveServiceTest {

    private val movieInfoService = MovieInfoService()
    private val reviewService = ReviewService()
    private val revenueService = RevenueService()
    private val movieReactiveService = MovieReactiveService(movieInfoService, reviewService, revenueService)

    @Test
    fun getAllMovies() {
        val moviesFlux = movieReactiveService.getAllMovies()
        StepVerifier.create(moviesFlux).assertNext { movie ->
            run {
                assertEquals("Batman Begins", movie.movieInfo.name)
                assertEquals(2, movie.reviewList.size)
            }
        }.assertNext { movie ->
            run {
                assertEquals("The Dark Knight", movie.movieInfo.name)
                assertEquals(2, movie.reviewList.size)
            }
        }.assertNext { movie ->
            run {
                assertEquals("Dark Knight Rises", movie.movieInfo.name)
                assertEquals(2, movie.reviewList.size)
            }
        }.verifyComplete()
    }

    @Test
    fun getMovieById() {
        val movieMono = movieReactiveService.getMovieById(100L)
        StepVerifier.create(movieMono).assertNext { movie ->
            run {
                assertEquals("Batman Begins", movie.movieInfo.name)
                assertEquals(2, movie.reviewList.size)
            }
        }.verifyComplete()
    }

    @Test
    fun getMovieByIdFlatMap() {
        val movieMono = movieReactiveService.getMovieByIdFlatMap(100L)
        StepVerifier.create(movieMono).assertNext { movie ->
            run {
                assertEquals("Batman Begins", movie.movieInfo.name)
                assertEquals(2, movie.reviewList.size)
            }
        }.verifyComplete()
    }

    @Test
    fun getMovieByIdWithRevenue() {
        val movieMono = movieReactiveService.getMovieByIdWithRevenue(100L).log()
        StepVerifier.create(movieMono).assertNext { movie ->
            assertEquals("Batman Begins", movie.movieInfo.name)
            assertEquals(2, movie.reviewList.size)
            assertNotNull(movie.revenue)
        }.verifyComplete()
    }
}