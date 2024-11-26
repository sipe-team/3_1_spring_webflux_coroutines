package com.sipe.week4

import kotlinx.coroutines.*

class SuspendExample {
}

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job1 = launch {
        delayAndPrintHelloCoroutines()
    }
    val job2 = launch {
        delayAndPrintHelloCoroutines()
    }
    job1.join()
    job2.join()
    println("${System.currentTimeMillis() - startTime}")
}

suspend fun delayAndPrintHelloCoroutines() {
    delay(100L)
    println("Hello Coroutines")
}