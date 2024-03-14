package com.miroshnychenko.exception

class NetworkException : RuntimeException {
    override var message: String?

    constructor(message: String?) : super(message) {
        this.message = message
    }

    constructor(ex: Throwable) : super(ex) {
        this.message = ex.message
    }
}
