package com.miroshnychenko.service

import com.miroshnychenko.domain.Movie
import com.miroshnychenko.domain.Revenue
import com.miroshnychenko.exception.MovieException
import com.miroshnychenko.exception.NetworkException
import com.miroshnychenko.exception.ServiceException
import reactor.core.Exceptions
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import java.time.Duration

class MovieReactiveService(private val movieInfoService: MovieInfoService, private val reviewService: ReviewService) {

    private lateinit var revenueService: RevenueService

    constructor(
        movieInfoService: MovieInfoService,
        reviewService: ReviewService,
        revenueService: RevenueService
    ) : this(movieInfoService, reviewService) {
        this.revenueService = revenueService
    }

    fun getAllMovies(): Flux<Movie> = movieInfoService.retrieveMoviesFlux().flatMap { mi ->
        reviewService.retrieveReviewsFlux(mi.movieInfoId).collectList().map { Movie(mi, it) }
    }.onErrorMap { MovieException(it.message) }.log()

    fun getAllMoviesRepeat(): Flux<Movie> = movieInfoService.retrieveMoviesFlux().flatMap { mi ->
        reviewService.retrieveReviewsFlux(mi.movieInfoId).collectList().map { Movie(mi, it) }
    }.repeat().onErrorMap { MovieException(it.message) }.log()

    fun getAllMoviesRepeatN(n: Long): Flux<Movie> = movieInfoService.retrieveMoviesFlux().flatMap { mi ->
        reviewService.retrieveReviewsFlux(mi.movieInfoId).collectList().map { Movie(mi, it) }
    }.repeat(n).onErrorMap { MovieException(it.message) }.log()

    fun getAllMoviesRetry(): Flux<Movie> = movieInfoService.retrieveMoviesFlux().flatMap { mi ->
        reviewService.retrieveReviewsFlux(mi.movieInfoId).collectList().map { Movie(mi, it) }
    }.onErrorMap { MovieException(it.message) }.retry(3).log()

    fun getAllMoviesRetryWhen(): Flux<Movie> {
        return movieInfoService.retrieveMoviesFlux().flatMap { mi ->
            reviewService.retrieveReviewsFlux(mi.movieInfoId).collectList().map { Movie(mi, it) }
        }.onErrorMap {
            if (it is NetworkException)

                MovieException(it.message)
            else
                ServiceException(it.message)
        }
            .retryWhen(retryBackoffSpec()).log()
    }

    private fun retryBackoffSpec(): RetryBackoffSpec =
        Retry.fixedDelay(3, Duration.ofMillis(500)).filter { it is MovieException }
            .onRetryExhaustedThrow { _, signal -> Exceptions.propagate(signal.failure()) }

    fun getMovieById(id: Long): Mono<Movie> {
        val movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(id)
        val reviewsFlux = reviewService.retrieveReviewsFlux(id).collectList()
        return movieInfoMono.zipWith(reviewsFlux) { movieInfo, reviews -> Movie(movieInfo, reviews) }
    }

    fun getMovieByIdFlatMap(id: Long): Mono<Movie> {
        return movieInfoService.retrieveMovieInfoMonoUsingId(id)
            .flatMap { mi -> reviewService.retrieveReviewsFlux(mi.movieInfoId).collectList().map { Movie(mi, it) } }
    }

    fun getMovieByIdWithRevenue(id: Long): Mono<Movie> {
        val movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(id)
        val reviewsFlux = reviewService.retrieveReviewsFlux(id).collectList()
        val revenueMono = Mono.fromCallable { revenueService.getRevenue(id) }.subscribeOn(Schedulers.boundedElastic())
        return movieInfoMono.zipWith(reviewsFlux) { movieInfo, reviews -> Movie(movieInfo, reviews) }
            .zipWith(revenueMono) { movie, revenue ->
                movie.revenue = revenue
                movie
            }
    }
}