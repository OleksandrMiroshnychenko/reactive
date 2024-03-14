package com.miroshnychenko

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import reactor.core.publisher.BaseSubscriber

import reactor.core.publisher.Flux
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

class BackpressureTest {
    private val log = KotlinLogging.logger {}

    @Test
    fun testBackPressure() {
        val numberRange = Flux.range(1, 100).log()

        numberRange
            //.subscribe { n -> log.info { "Number is $n" } }
            .subscribe(object : BaseSubscriber<Int>() {
                override fun hookOnSubscribe(subscription: Subscription) {
//                    super.hookOnSubscribe(subscription)
                    request(2)
                }

                override fun hookOnNext(value: Int) {
//                    super.hookOnNext(value)
                    log.info { "hookOnNext: $value" }
                    if (value == 2) {
                        cancel()
                    }
                }

                override fun hookOnComplete() {
//                    super.hookOnComplete()
                }

                override fun hookOnError(throwable: Throwable) {
//                    super.hookOnError(throwable)
                }

                override fun hookOnCancel() {
//                    super.hookOnCancel()
                    log.info { "Inside OnCancel" }
                }
            })
    }

    @Test
    fun testBackPressure2() {
        val numberRange = Flux.range(1, 100).log()

        val latch = CountDownLatch(1)

        numberRange
            .subscribe(object : BaseSubscriber<Int>() {
                override fun hookOnSubscribe(subscription: Subscription) {
//                    super.hookOnSubscribe(subscription)
                    request(2)
                }

                override fun hookOnNext(value: Int) {
//                    super.hookOnNext(value)
                    log.info { "hookOnNext: $value" }
                    if (value % 2 == 0 || value < 50) {
                        request(2)
                    } else {
                        cancel()
                    }
                }

                override fun hookOnComplete() {
//                    super.hookOnComplete()
                }

                override fun hookOnError(throwable: Throwable) {
//                    super.hookOnError(throwable)
                }

                override fun hookOnCancel() {
//                    super.hookOnCancel()
                    log.info { "Inside OnCancel" }
                    latch.countDown()
                }
            })
        assertTrue(latch.await(5L, TimeUnit.SECONDS))
    }

    @Test
    fun testBackPressureDrop() {
        val numberRange = Flux.range(1, 100).log()

        val latch = CountDownLatch(1)

        numberRange
            .onBackpressureDrop { log.info { "Dropped item is: $it" } }
            .subscribe(object : BaseSubscriber<Int>() {
                override fun hookOnSubscribe(subscription: Subscription) {
//                    super.hookOnSubscribe(subscription)
                    request(2)
                }

                override fun hookOnNext(value: Int) {
//                    super.hookOnNext(value)
                    log.info { "hookOnNext: $value" }
                    if (value == 2) {
                        hookOnCancel()
                    }
                }

                override fun hookOnComplete() {
//                    super.hookOnComplete()
                }

                override fun hookOnError(throwable: Throwable) {
//                    super.hookOnError(throwable)
                }

                override fun hookOnCancel() {
//                    super.hookOnCancel()
                    log.info { "Inside OnCancel" }
                    latch.countDown()
                }
            })
        assertTrue(latch.await(5L, TimeUnit.SECONDS))
    }

    @Test
    fun testBackPressureBuffer() {
        val numberRange = Flux.range(1, 100).log()

        val latch = CountDownLatch(1)

        numberRange
            .onBackpressureBuffer(10) { log.info { "Last buffered element is: $it" } }
            .subscribe(object : BaseSubscriber<Int>() {
                override fun hookOnSubscribe(subscription: Subscription) {
//                    super.hookOnSubscribe(subscription)
                    request(1)
                }

                override fun hookOnNext(value: Int) {
//                    super.hookOnNext(value)
                    log.info { "hookOnNext: $value" }
                    if (value < 50) {
                        request(1)
                    } else {
                        hookOnCancel()
                    }
                }

                override fun hookOnComplete() {
//                    super.hookOnComplete()
                }

                override fun hookOnError(throwable: Throwable) {
//                    super.hookOnError(throwable)
                }

                override fun hookOnCancel() {
//                    super.hookOnCancel()
                    log.info { "Inside OnCancel" }
                    latch.countDown()
                }
            })
        assertTrue(latch.await(5L, TimeUnit.SECONDS))
    }

    @Test
    fun testBackPressureError() {
        val numberRange = Flux.range(1, 100).log()

        val latch = CountDownLatch(1)

        numberRange
            .onBackpressureError()
            .subscribe(object : BaseSubscriber<Int>() {
                override fun hookOnSubscribe(subscription: Subscription) {
//                    super.hookOnSubscribe(subscription)
                    request(1)
                }

                override fun hookOnNext(value: Int) {
//                    super.hookOnNext(value)
                    log.info { "hookOnNext: $value" }
                    if (value < 50) {
                        request(1)
                    } else {
                        hookOnCancel()
                    }
                }

                override fun hookOnComplete() {
//                    super.hookOnComplete()
                }

                override fun hookOnError(throwable: Throwable) {
//                    super.hookOnError(throwable)
                    log.error { "Exception is: $throwable" }
                }

                override fun hookOnCancel() {
//                    super.hookOnCancel()
                    log.info { "Inside OnCancel" }
                    latch.countDown()
                }
            })
        assertTrue(latch.await(5L, TimeUnit.SECONDS))
    }
}
