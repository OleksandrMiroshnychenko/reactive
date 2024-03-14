package com.miroshnychenko.service

import com.miroshnychenko.exception.ReactorException
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.scheduler.VirtualTimeScheduler
import java.time.Duration

class FluxAndMonoGeneratorServiceTest {
    private val fluxAndMonoGeneratorService = FluxAndMonoGeneratorService()

    @Test
    fun namesFlux() {
        val namesFlux = fluxAndMonoGeneratorService.namesFlux()
        StepVerifier.create(namesFlux)
//            .expectNext("alex", "ben", "chloe")
//            .expectNextCount(3)
            .expectNext("alex").expectNextCount(2).verifyComplete()
    }

    @Test
    fun namesFluxMap() {
        val namesFlux = fluxAndMonoGeneratorService.namesFluxMap(3)
        StepVerifier.create(namesFlux).expectNext("4-ALEX", "5-CHLOE").verifyComplete()
    }

    @Test
    fun namesFluxFlatMap() {
        val namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMap(3)
        StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E").verifyComplete()
    }

    @Test
    fun namesFluxTransform() {
        val namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(3)
        StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E").verifyComplete()
    }

    @Test
    fun namesFluxTransformDefaultIfEmpty() {
        val namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(5)
        StepVerifier.create(namesFlux).expectNext("default").verifyComplete()
    }

    @Test
    fun namesFluxTransformSwitchIfEmpty() {
        val namesFlux = fluxAndMonoGeneratorService.namesFluxTransformSwitchIfEmpty(5)
        StepVerifier.create(namesFlux).expectNext("D", "E", "F", "A", "U", "L", "T").verifyComplete()
    }

    @Test
    fun namesFluxFlatMapAsync() {
        val namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMapAsync(3)
        StepVerifier.create(namesFlux).expectNextCount(9).verifyComplete()
    }

    @Test
    fun namesFluxConcatMap() {
        val namesFlux = fluxAndMonoGeneratorService.namesFluxConcatMap(3)
        StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E").verifyComplete()
    }

    @Test
    fun namesFluxImmutability() {
        val namesFlux = fluxAndMonoGeneratorService.namesFluxImmutability()
        StepVerifier.create(namesFlux).expectNext("alex", "ben", "chloe").verifyComplete()
    }

    @Test
    fun exploreConcat() {
        val concatFlux = fluxAndMonoGeneratorService.exploreConcat()

        StepVerifier.create(concatFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete()
    }

    @Test
    fun exploreConcatWith() {
        val concatFlux = fluxAndMonoGeneratorService.exploreConcatWith()

        StepVerifier.create(concatFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete()
    }

    @Test
    fun exploreConcatWithMono() {
        val concatFlux = fluxAndMonoGeneratorService.exploreConcatWithMono()

        StepVerifier.create(concatFlux).expectNext("A", "B").verifyComplete()
    }

    @Test
    fun exploreMerge() {
        val mergeFlux = fluxAndMonoGeneratorService.exploreMerge()

        StepVerifier.create(mergeFlux).expectNext("A", "D", "B", "E", "C", "F").verifyComplete()
    }

    @Test
    fun exploreMergeWith() {
        val mergeFlux = fluxAndMonoGeneratorService.exploreMergeWith()

        StepVerifier.create(mergeFlux).expectNext("A", "D", "B", "E", "C", "F").verifyComplete()
    }

    @Test
    fun exploreMergeWithMono() {
        val mergeFlux = fluxAndMonoGeneratorService.exploreMergeWithMono()

        StepVerifier.create(mergeFlux).expectNextCount(2).verifyComplete()
    }

    @Test
    fun exploreMergeSequential() {
        val mergeFlux = fluxAndMonoGeneratorService.exploreMergeSequential()

        StepVerifier.create(mergeFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete()
    }

    @Test
    fun exploreZip() {
        val zipFlux = fluxAndMonoGeneratorService.exploreZip()

        StepVerifier.create(zipFlux).expectNext("AD", "BE", "CF").verifyComplete()
    }

    @Test
    fun exploreZip4() {
        val zipFlux = fluxAndMonoGeneratorService.exploreZip4()

        StepVerifier.create(zipFlux).expectNext("AD14", "BE25", "CF36").verifyComplete()
    }

    @Test
    fun exploreZipWith() {
        val zipFlux = fluxAndMonoGeneratorService.exploreZipWith()

        StepVerifier.create(zipFlux).expectNext("AD", "BE", "CF").verifyComplete()
    }

    @Test
    fun exploreZipWithMono() {
        val zipMono = fluxAndMonoGeneratorService.exploreZipWithMono()

        StepVerifier.create(zipMono).expectNext("AB").verifyComplete()
    }

    @Test
    fun nameMono() {
        val nameMono = fluxAndMonoGeneratorService.nameMono()
        StepVerifier.create(nameMono).expectNext("alex").verifyComplete()
    }

    @Test
    fun namesMonoMapFilter() {
        val nameMono = fluxAndMonoGeneratorService.nameMonoMapFilter(3)
        StepVerifier.create(nameMono).expectNext("ALEX").verifyComplete()

        val nameMono2 = fluxAndMonoGeneratorService.nameMonoMapFilter(4)
        StepVerifier.create(nameMono2).expectNext("default").verifyComplete()
    }

    @Test
    fun namesMonoMapFilterDefaultIfEmpty() {
        val nameMono = fluxAndMonoGeneratorService.nameMonoMapFilter(4)
        StepVerifier.create(nameMono).expectNext("default").verifyComplete()
    }

    @Test
    fun namesMonoMapFilterSwitchIfEmpty() {
        val nameMono = fluxAndMonoGeneratorService.nameMonoMapFilterSwitchIfEmpty(4)
        StepVerifier.create(nameMono).expectNext(listOf("D", "E", "F", "A", "U", "L", "T")).verifyComplete()
    }

    @Test
    fun namesMonoFlatMap() {
        val nameMono = fluxAndMonoGeneratorService.nameMonoFlatMap(3)
        StepVerifier.create(nameMono).expectNext(listOf("A", "L", "E", "X")).verifyComplete()
    }

    @Test
    fun namesMonoFlatMapMany() {
        val nameMono = fluxAndMonoGeneratorService.nameMonoFlatMapMany(3)
        StepVerifier.create(nameMono).expectNext("A", "L", "E", "X").verifyComplete()
    }

    @Test
    fun runtimeExceptionFlux() {
        val exceptionFlux = fluxAndMonoGeneratorService.exceptionFlux()
        StepVerifier.create(exceptionFlux).expectNext("A", "B", "C")
            .expectError(RuntimeException::class.java)
            .verify()
    }

    @Test
    fun exceptionFlux() {
        val exceptionFlux = fluxAndMonoGeneratorService.exceptionFlux()
        StepVerifier.create(exceptionFlux).expectNext("A", "B", "C")
            .expectError()
            .verify()
    }

    @Test
    fun exceptionFluxWithMessage() {
        val exceptionFlux = fluxAndMonoGeneratorService.exceptionFlux()
        StepVerifier.create(exceptionFlux).expectNext("A", "B", "C")
            .expectErrorMessage("Exception occurred in Flux")
            .verify()
    }

    @Test
    fun exploreOnErrorReturn() {
        val exceptionFlux = fluxAndMonoGeneratorService.exploreOnErrorReturn()
        StepVerifier.create(exceptionFlux).expectNext("A", "B", "C", "D").verifyComplete()
    }

    @Test
    fun exploreOnErrorResume() {
        val exceptionFlux = fluxAndMonoGeneratorService.exploreOnErrorResume(IllegalStateException("Not a valid state"))
        StepVerifier.create(exceptionFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete()
    }

    @Test
    fun exploreOnErrorResumeExceptionType() {
        val exceptionFlux = fluxAndMonoGeneratorService.exploreOnErrorResume(RuntimeException("Runtime exception"))
        StepVerifier.create(exceptionFlux).expectNext("A", "B", "C").expectError(RuntimeException::class.java).verify()
    }

    @Test
    fun exploreOnErrorContinue() {
        val exceptionFlux = fluxAndMonoGeneratorService.exploreOnErrorContinue()
        StepVerifier.create(exceptionFlux).expectNext("A", "C", "D").verifyComplete()
    }

    @Test
    fun exploreOnErrorMap() {
        val exceptionFlux = fluxAndMonoGeneratorService.exploreOnErrorMap()
        StepVerifier.create(exceptionFlux).expectNext("A").expectError(ReactorException::class.java).verify()
    }

    @Test
    fun exploreDoOnError() {
        val exceptionFlux = fluxAndMonoGeneratorService.exploreDoOnError()
        StepVerifier.create(exceptionFlux).expectNext("A", "B", "C").expectError(RuntimeException::class.java).verify()
    }

    @Test
    fun exploreMonoOnErrorReturn() {
        val exceptionMono = fluxAndMonoGeneratorService.exploreMonoOnErrorReturn()
        StepVerifier.create(exceptionMono).expectNext("abc").verifyComplete()
    }

    @Test
    fun exploreMonoOnErrorMap() {
        val exceptionMono =
            fluxAndMonoGeneratorService.exploreMonoOnErrorMap(RuntimeException("Exception occurred in Mono"))
        StepVerifier.create(exceptionMono).expectError(ReactorException::class.java).verify()
    }

    @Test
    fun exploreMonoOnErrorContinue() {
        val exceptionMono = fluxAndMonoGeneratorService.exploreMonoOnErrorContinue("reactor")
        StepVerifier.create(exceptionMono).expectNext("reactor").verifyComplete()
    }

    @Test
    fun exploreMonoOnErrorContinueException() {
        val exceptionMono = fluxAndMonoGeneratorService.exploreMonoOnErrorContinue("abc")
        StepVerifier.create(exceptionMono).verifyComplete()
    }

    @Test
    fun namesFluxConcatMapVirtualTimer() {
        VirtualTimeScheduler.getOrSet()
        val namesFlux = fluxAndMonoGeneratorService.namesFluxConcatMap(3)
        StepVerifier.withVirtualTime { namesFlux }
            .thenAwait(Duration.ofSeconds(10))
            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .verifyComplete()
    }
}
