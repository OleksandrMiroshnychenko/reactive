package com.miroshnychenko.service

import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class ReviewServiceTest {

    private val webClient = WebClient.builder().baseUrl("http://localhost:8080/movies").build()
    private val reviewService = ReviewService(webClient)

    @Test
    fun retrieveReviewsFluxRestClient() {
        val flux = reviewService.retrieveReviewsFluxRestClient(1)

        StepVerifier.create(flux).expectNextCount(1).verifyComplete()
    }
}