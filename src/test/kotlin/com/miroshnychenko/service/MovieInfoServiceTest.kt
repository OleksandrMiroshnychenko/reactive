package com.miroshnychenko.service

import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class MovieInfoServiceTest {

    private val webClient = WebClient.builder().baseUrl("http://localhost:8080/movies").build()

    private val movieInfoService = MovieInfoService(webClient)

    @Test
    fun retrieveAllMovieInfoRestClient() {
        val flux = movieInfoService.retrieveAllMovieInfoRestClient()

        StepVerifier.create(flux).expectNextCount(7).verifyComplete()
    }

    @Test
    fun retrieveMovieInfoByIdRestClient() {
        val mono = movieInfoService.retrieveMovieInfoByIdRestClient(1)

        StepVerifier.create(mono).expectNextCount(1).verifyComplete()
    }
}