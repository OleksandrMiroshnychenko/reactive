package com.miroshnychenko.service

import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class MovieReactiveServiceRestClientTest {

    private val webClient = WebClient.builder().baseUrl("http://localhost:8080/movies").build()

    private val movieInfoService = MovieInfoService(webClient)

    private val reviewService = ReviewService(webClient)

    private val movieReactiveService = MovieReactiveService(movieInfoService, reviewService)

    @Test
    fun getAllMoviesRestClient() {
        val moviesFlux = movieReactiveService.getAllMoviesRestClient()

        StepVerifier.create(moviesFlux).expectNextCount(7).verifyComplete()
    }

    @Test
    fun getMovieByIdRestClient() {
        val moviesMono = movieReactiveService.getMovieByIdRestClient(1)

        StepVerifier.create(moviesMono).expectNextCount(1).verifyComplete()
    }
}