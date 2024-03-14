package com.miroshnychenko.exception

class ReactorException(private val exception: Throwable, override val message: String?) : Throwable()