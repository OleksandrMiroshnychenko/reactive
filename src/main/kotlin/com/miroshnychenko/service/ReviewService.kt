package com.miroshnychenko.service

import com.miroshnychenko.domain.Review
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux

class ReviewService() {

    private lateinit var webClient: WebClient

    constructor(webClient: WebClient): this() {
        this.webClient = webClient
    }

    fun retrieveReviewsFluxRestClient(movieInfoId: Long): Flux<Review> {
        val uri = UriComponentsBuilder.fromUriString("/v1/reviews")
            .queryParam("movieInfoId", movieInfoId)
            .buildAndExpand()
            .toUriString()

        return webClient.get().uri(uri)
            .retrieve()
            .bodyToFlux(Review::class.java)
    }

    fun retrieveReviews(movieInfoId: Long): List<Review> {
        return listOf(
            Review(1L, movieInfoId, "Awesome Movie", 8.9), Review(2L, movieInfoId, "Excellent Movie", 9.0)
        )
    }

    fun retrieveReviewsFlux(movieInfoId: Long): Flux<Review> {
        val reviewsList = listOf(
            Review(1L, movieInfoId, "Awesome Movie", 8.9), Review(2L, movieInfoId, "Excellent Movie", 9.0)
        )
        return Flux.fromIterable(reviewsList)
    }
}
