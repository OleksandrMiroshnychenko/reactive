package com.miroshnychenko.util

object CommonUtil {
    fun delay(ms: Int) {
        try {
            Thread.sleep(ms.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
