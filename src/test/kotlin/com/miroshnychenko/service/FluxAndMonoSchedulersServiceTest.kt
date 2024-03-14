package com.miroshnychenko.service

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class FluxAndMonoSchedulersServiceTest {

    private val fluxAndMonoSchedulersService = FluxAndMonoSchedulersService()

    @Test
    fun explorePublishOn() {
        val flux = fluxAndMonoSchedulersService.explorePublishOn()

        StepVerifier.create(flux).expectNextCount(6).verifyComplete()
    }

    @Test
    fun exploreSubscribeOn() {
        val flux = fluxAndMonoSchedulersService.exploreSubscribeOn()

        StepVerifier.create(flux).expectNextCount(6).verifyComplete()
    }

    @Test
    fun exploreParallel() {
        val flux = fluxAndMonoSchedulersService.exploreParallel()

        StepVerifier.create(flux).expectNextCount(3).verifyComplete()
    }

    @Test
    fun exploreParallelUsingFlatMap() {
        val flux = fluxAndMonoSchedulersService.exploreParallelUsingFlatMap()

        StepVerifier.create(flux).expectNextCount(3).verifyComplete()
    }

    @Test
    fun exploreParallelUsingFlatMap2() {
        val flux = fluxAndMonoSchedulersService.exploreParallelUsingFlatMap2()

        StepVerifier.create(flux).expectNextCount(6).verifyComplete()
    }

    @Test
    fun exploreParallelUsingFlatMapSequential() {
        val flux = fluxAndMonoSchedulersService.exploreParallelUsingFlatMapSequential()

        StepVerifier.create(flux).expectNext("ALEX", "BEN", "CHLOE").verifyComplete()
    }
}