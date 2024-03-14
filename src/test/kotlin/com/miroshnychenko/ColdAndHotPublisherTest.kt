package com.miroshnychenko

import com.miroshnychenko.util.CommonUtil.delay
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.Duration

class ColdAndHotPublisherTest {

    @Test
    fun coldPublisherTest() {

        val flux = Flux.range(1, 10)

        flux.subscribe { println("Subscriber 1: $it") }
        flux.subscribe { println("Subscriber 2: $it") }
    }

    @Test
    fun hotPublisherTest() {

        val connectableFlux = Flux.range(1, 10).delayElements(Duration.ofSeconds(1)).publish()
        connectableFlux.connect()

        connectableFlux.subscribe { println("Subscriber 1: $it") }
        delay(4000)
        connectableFlux.subscribe { println("Subscriber 2: $it") }
        delay(7000)
    }

    @Test
    fun hotPublisherTestAutoConnect() {

        val flux = Flux.range(1, 10).delayElements(Duration.ofSeconds(1)).publish().autoConnect(2)

        flux.subscribe { println("Subscriber 1: $it") }
        delay(2000)
        flux.subscribe { println("Subscriber 2: $it") }
        println("Two subscribers are connected")
        delay(2000)
        flux.subscribe { println("Subscriber 3: $it") }
        delay(10000)
    }

    @Test
    fun hotPublisherTestRefCount() {

        val flux = Flux.range(1, 10).delayElements(Duration.ofSeconds(1)).doOnCancel { println("Received cancel signal") }.publish().refCount(2)

        val disposable = flux.subscribe { println("Subscriber 1: $it") }
        delay(2000)
        val disposable2 = flux.subscribe { println("Subscriber 2: $it") }
        println("Two subscribers are connected")
        delay(2000)
        disposable.dispose()
        disposable2.dispose()
        flux.subscribe { println("Subscriber 3: $it") }
        delay(2000)
        flux.subscribe { println("Subscriber 4: $it") }
        delay(10000)
    }
}
