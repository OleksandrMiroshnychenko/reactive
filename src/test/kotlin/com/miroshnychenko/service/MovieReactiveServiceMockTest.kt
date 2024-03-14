package com.miroshnychenko.service

import com.miroshnychenko.exception.MovieException
import com.miroshnychenko.exception.NetworkException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class MovieReactiveServiceMockTest {

    private val movieInfoService: MovieInfoService = mockk()

    private val reviewService: ReviewService = mockk()

    private val movieReactiveService = MovieReactiveService(movieInfoService, reviewService)

    @Test
    fun getAllMovies() {
        every { movieInfoService.retrieveMoviesFlux() } answers { callOriginal() }
        every { reviewService.retrieveReviewsFlux(any()) } answers { callOriginal() }

        val moviesFlux = movieReactiveService.getAllMovies()

        StepVerifier.create(moviesFlux).expectNextCount(3).verifyComplete()
    }

    @Test
    fun getAllMoviesRepeat() {
        every { movieInfoService.retrieveMoviesFlux() } answers { callOriginal() }
        every { reviewService.retrieveReviewsFlux(any()) } answers { callOriginal() }

        val moviesFlux = movieReactiveService.getAllMoviesRepeat()

        StepVerifier.create(moviesFlux).expectNextCount(9).thenCancel().verify()

        verify(exactly = 9) { reviewService.retrieveReviewsFlux(any()) }
    }

    @Test
    fun getAllMoviesRepeatN() {
        every { movieInfoService.retrieveMoviesFlux() } answers { callOriginal() }
        every { reviewService.retrieveReviewsFlux(any()) } answers { callOriginal() }

        val moviesFlux = movieReactiveService.getAllMoviesRepeatN(2)

        StepVerifier.create(moviesFlux).expectNextCount(9).verifyComplete()

        verify(exactly = 9) { reviewService.retrieveReviewsFlux(any()) }
    }

    @Test
    fun getAllMoviesException() {
        val exceptionMessage = "Exception occurred in ReviewService"

        every { movieInfoService.retrieveMoviesFlux() } answers { callOriginal() }
        every { reviewService.retrieveReviewsFlux(any()) } throws (RuntimeException(exceptionMessage))

        val moviesFlux = movieReactiveService.getAllMovies()
        StepVerifier.create(moviesFlux).expectError(MovieException::class.java).verify()
    }

    @Test
    fun getAllMoviesExceptionRetry() {
        val exceptionMessage = "Exception occurred in ReviewService"

        every { movieInfoService.retrieveMoviesFlux() } answers { callOriginal() }
        every { reviewService.retrieveReviewsFlux(any()) } throws (RuntimeException(exceptionMessage))

        val moviesFlux = movieReactiveService.getAllMoviesRetry()
        StepVerifier.create(moviesFlux).expectError(MovieException::class.java).verify()

        verify(exactly = 4) { reviewService.retrieveReviewsFlux(any()) }
    }

    @Test
    fun getAllMoviesExceptionRetryWhenMovieException() {
        val exceptionMessage = "Exception occurred in ReviewService"

        every { movieInfoService.retrieveMoviesFlux() } answers { callOriginal() }
        every { reviewService.retrieveReviewsFlux(any()) } throws (NetworkException(exceptionMessage))

        val moviesFlux = movieReactiveService.getAllMoviesRetryWhen()
        StepVerifier.create(moviesFlux).expectError(MovieException::class.java).verify()

        verify(exactly = 4) { reviewService.retrieveReviewsFlux(any()) }
    }

    @Test
    fun getAllMoviesExceptionRetryWhenException() {
        val exceptionMessage = "Exception occurred in ReviewService"

        every { movieInfoService.retrieveMoviesFlux() } answers { callOriginal() }
        every { reviewService.retrieveReviewsFlux(any()) } throws (RuntimeException(exceptionMessage))

        val moviesFlux = movieReactiveService.getAllMoviesRetryWhen()
        StepVerifier.create(moviesFlux).expectError(RuntimeException::class.java).verify()

        verify(exactly = 1) { reviewService.retrieveReviewsFlux(any()) }
    }
}