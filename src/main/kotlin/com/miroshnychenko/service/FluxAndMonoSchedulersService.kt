package com.miroshnychenko.service

import com.miroshnychenko.util.CommonUtil.delay
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.ParallelFlux
import reactor.core.scheduler.Schedulers
import java.util.*

class FluxAndMonoSchedulersService {

    companion object {
        val namesList = listOf("alex", "ben", "chloe")
        val namesList1 = listOf("adam", "jill", "jack")
    }

    fun explorePublishOn(): Flux<String> {
        val namesFlux = Flux.fromIterable(namesList).publishOn(Schedulers.parallel()).map(this::upperCase).log()
        val namesFlux1 = Flux.fromIterable(namesList1).publishOn(Schedulers.parallel()).map(this::upperCase)
            .doOnEach { println("Name is: $it") }.log()
        return namesFlux.mergeWith(namesFlux1)
    }

    fun exploreSubscribeOn(): Flux<String> {
        val namesFlux = getFlux(namesList).subscribeOn(Schedulers.boundedElastic()).log()
        val namesFlux1 =
            getFlux(namesList1).subscribeOn(Schedulers.boundedElastic()).doOnEach { println("Name is: $it") }.log()
        return namesFlux.mergeWith(namesFlux1)
    }

    fun exploreParallel(): ParallelFlux<String> {
        val namesFlux = Flux.fromIterable(namesList).parallel().runOn(Schedulers.parallel()).map(this::upperCase).log()
        return namesFlux
    }

    fun exploreParallelUsingFlatMap(): Flux<String> {
        val namesFlux = Flux.fromIterable(namesList)
            .flatMap { Mono.just(it).map(this::upperCase).subscribeOn(Schedulers.parallel()) }.log()
        return namesFlux
    }

    fun exploreParallelUsingFlatMap2(): Flux<String> {
        val namesFlux = Flux.fromIterable(namesList)
            .flatMap { Mono.just(it).map(this::upperCase).subscribeOn(Schedulers.parallel()) }.log()
        val namesFlux1 = Flux.fromIterable(namesList1)
            .flatMap { Mono.just(it).map(this::upperCase).subscribeOn(Schedulers.parallel()) }
            .doOnEach { println("Name is: $it") }.log()
        return namesFlux.mergeWith(namesFlux1)
    }

    fun exploreParallelUsingFlatMapSequential(): Flux<String> {
        val namesFlux = Flux.fromIterable(namesList)
            .flatMapSequential { Mono.just(it).map(this::upperCase).subscribeOn(Schedulers.parallel()) }.log()
        return namesFlux
    }

    private fun getFlux(list: List<String>): Flux<String> = Flux.fromIterable(list).map(this::upperCase)

    private fun upperCase(name: String): String {
        delay(1000)
        return name.uppercase(Locale.getDefault())
    }
}
