package com.miroshnychenko.service

import com.miroshnychenko.exception.ReactorException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import kotlin.random.Random

class FluxAndMonoGeneratorService {

    fun namesFlux(): Flux<String> = Flux.fromIterable(listOf("alex", "ben", "chloe")).log()

    fun namesFluxMap(stringLength: Int): Flux<String> =
        Flux.fromIterable(listOf("alex", "ben", "chloe")).map(String::uppercase).filter { it.length > stringLength }
            .map { "${it.length}-$it" }.doOnNext { println("Name is: $it") }
            .doOnSubscribe { println("Subscription is: $it") }.doOnComplete { println("Inside the complete callback") }
            .doFinally { println("Inside doFinally: $it") }.log()

    fun namesFluxFlatMap(stringLength: Int): Flux<String> =
        Flux.fromIterable(listOf("alex", "ben", "chloe")).map(String::uppercase).filter { it.length > stringLength }
            .flatMap(this::splitString).filter(String::isNotBlank).log()

    fun namesFluxTransform(stringLength: Int): Flux<String> =
        Flux.fromIterable(listOf("alex", "ben", "chloe")).transform {
            it.map(String::uppercase).filter { s -> s.length > stringLength }
        }.flatMap(this::splitString).filter(String::isNotBlank).defaultIfEmpty("default").log()

    fun namesFluxTransformSwitchIfEmpty(stringLength: Int): Flux<String> {
        val transform: (t: Flux<String>) -> Flux<String> = {
            it.map(String::uppercase).filter { s -> s.length > stringLength }.flatMap(this::splitString)
                .filter(String::isNotBlank)
        }

        return Flux.fromIterable(listOf("alex", "ben", "chloe")).transform(transform)
            .switchIfEmpty(Flux.just("default").transform(transform)).log()
    }

    private fun splitString(name: String): Flux<String> = Flux.fromIterable(name.split(""))

    fun namesFluxFlatMapAsync(stringLength: Int): Flux<String> =
        Flux.fromIterable(listOf("alex", "ben", "chloe")).map(String::uppercase).filter { it.length > stringLength }
            .flatMap(this::splitStringWithDelay).filter(String::isNotBlank).log()

    fun namesFluxConcatMap(stringLength: Int): Flux<String> =
        Flux.fromIterable(listOf("alex", "ben", "chloe")).map(String::uppercase).filter { it.length > stringLength }
            .concatMap(this::splitStringWithDelay).filter(String::isNotBlank).log()

    private fun splitStringWithDelay(name: String): Flux<String> = Flux.fromIterable(name.split("")).delayElements(
        Duration.ofMillis(Random.nextLong(1000))
    )

    fun namesFluxImmutability(): Flux<String> {
        val namesFluxImmutability = Flux.fromIterable(listOf("alex", "ben", "chloe"))
        namesFluxImmutability.map(String::uppercase)
        return namesFluxImmutability
    }

    fun exploreConcat(): Flux<String> {
        val abcFlux = Flux.just("A", "B", "C")
        val defFlux = Flux.just("D", "E", "F")
        return Flux.concat(abcFlux, defFlux)
    }

    fun exploreConcatWith(): Flux<String> {
        val abcFlux = Flux.just("A", "B", "C")
        val defFlux = Flux.just("D", "E", "F")
        return abcFlux.concatWith(defFlux)
    }

    fun exploreConcatWithMono(): Flux<String> {
        val aMono = Mono.just("A")
        val bMono = Mono.just("B")
        return aMono.concatWith(bMono)
    }

    fun exploreMerge(): Flux<String> {
        val abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100))
        val defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125))
        return Flux.merge(abcFlux, defFlux)
    }

    fun exploreMergeWith(): Flux<String> {
        val abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100))
        val defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125))
        return abcFlux.mergeWith(defFlux)
    }

    fun exploreMergeWithMono(): Flux<String> {
        val aMono = Mono.just("A")
        val bMono = Mono.just("B")
        return aMono.mergeWith(bMono)
    }

    fun exploreMergeSequential(): Flux<String> {
        val abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100))
        val defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125))
        return Flux.mergeSequential(abcFlux, defFlux)
    }

    fun exploreZip(): Flux<String> {
        val abcFlux = Flux.just("A", "B", "C")
        val defFlux = Flux.just("D", "E", "F")
        return Flux.zip(abcFlux, defFlux) { first, second -> first + second }
    }

    fun exploreZip4(): Flux<String> {
        val abcFlux = Flux.just("A", "B", "C")
        val defFlux = Flux.just("D", "E", "F")
        val flux123 = Flux.just("1", "2", "3")
        val flux456 = Flux.just("4", "5", "6")
        return Flux.zip(abcFlux, defFlux, flux123, flux456).map { it.t1 + it.t2 + it.t3 + it.t4 }
    }

    fun exploreZipWith(): Flux<String> {
        val abcFlux = Flux.just("A", "B", "C")
        val defFlux = Flux.just("D", "E", "F")
        return abcFlux.zipWith(defFlux) { first, second -> first + second }
    }

    fun exploreZipWithMono(): Mono<String> {
        val aMono = Mono.just("A")
        val bMono = Mono.just("B")
        return aMono.zipWith(bMono) { first, second -> first + second }
    }

    fun nameMono(): Mono<String> = Mono.just("alex").log()

    fun nameMonoMapFilter(stringLength: Int): Mono<String> =
        Mono.just("alex").map(String::uppercase).filter { it.length > stringLength }.defaultIfEmpty("default").log()

    fun nameMonoMapFilterSwitchIfEmpty(stringLength: Int): Mono<List<String>> {
        val transform: (t: Mono<String>) -> Mono<List<String>> = {
            it.map(String::uppercase).filter { s -> s.length > stringLength }.flatMap(this::splitStringMono)
        }
        return Mono.just("alex").transform(transform).switchIfEmpty(Mono.just("default").transform(transform)).log()
    }

    fun nameMonoFlatMap(stringLength: Int): Mono<List<String>> =
        Mono.just("alex").map(String::uppercase).filter { it.length > stringLength }.flatMap(this::splitStringMono)
            .log()

    fun nameMonoFlatMapMany(stringLength: Int): Flux<String> =
        Mono.just("alex").map(String::uppercase).filter { it.length > stringLength }.flatMapMany(this::splitString)
            .filter(String::isNotBlank).log()

    private fun splitStringMono(s: String): Mono<List<String>> = Mono.just(s.split("").filter(String::isNotBlank))

    fun exceptionFlux(): Flux<String> =
        Flux.just("A", "B", "C").concatWith(Flux.error(RuntimeException("Exception occurred in Flux")))
            .concatWith(Flux.just("D"))

    fun exploreOnErrorReturn(): Flux<String> =
        Flux.just("A", "B", "C").concatWith(Flux.error(RuntimeException("Exception occurred in Flux")))
            .onErrorReturn("D")

    fun exploreOnErrorResume(ex: Exception): Flux<String> =
        Flux.just("A", "B", "C").concatWith(Flux.error(ex)).onErrorResume {
            if (it is IllegalStateException) {
                Flux.just("D", "E", "F")
            } else {
                Flux.error(ex)
            }
        }

    fun exploreOnErrorContinue(): Flux<String> = Flux.just("A", "B", "C").map {
        if (it.equals("B")) throw IllegalStateException("Exception occurred in Flux")
        else it
    }.concatWith(Flux.just("D")).onErrorContinue { ex, s ->
        println(ex.message)
        println(s)
    }

    fun exploreOnErrorMap(): Flux<String> = Flux.just("A", "B", "C").map {
        if (it.equals("B")) throw IllegalStateException("Exception occurred in Flux")
        else it
    }.concatWith(Flux.just("D")).onErrorMap {
        println(it.message)
        throw ReactorException(it, it.message)
    }

    fun exploreDoOnError(): Flux<String> =
        Flux.just("A", "B", "C").concatWith(Flux.error(RuntimeException("Exception occurred in Flux")))
            .doOnError { println(it.message) }

    fun exploreMonoOnErrorReturn(): Mono<String> =
        Mono.just("A").map { if (it.equals("A")) throw RuntimeException("Exception occurred in Mono") else it }
            .onErrorReturn("abc")

    fun exploreMonoOnErrorMap(ex: Exception): Mono<String> =
        Mono.just("B").map { if (it.equals("B")) throw ex else it }.onErrorMap { ReactorException(it, it.message) }

    fun exploreMonoOnErrorContinue(string: String): Mono<String> =
        Mono.just(string).map { if (it.equals("abc")) throw RuntimeException("Exception occurred in Mono") else it }
            .onErrorContinue { ex, s ->
                println(ex.message)
                println(s)
            }
}

fun main() {
    val fluxAndMonoGeneratorService = FluxAndMonoGeneratorService()
    fluxAndMonoGeneratorService.namesFlux().subscribe { println("Name is: $it") }
    fluxAndMonoGeneratorService.nameMono().subscribe { println("Mono name is: $it") }
}