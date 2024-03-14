package com.miroshnychenko.service

import com.miroshnychenko.domain.Review
import reactor.core.publisher.Flux

class ReviewService {
    fun retrieveReviews(movieInfoId: Long): List<Review> {
        return java.util.List.of<Review>(
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
