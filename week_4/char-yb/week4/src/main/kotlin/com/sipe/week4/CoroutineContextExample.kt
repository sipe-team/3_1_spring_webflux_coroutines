package com.sipe.week4

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CoroutineContextExample {
    private val customContext = CoroutineName("나만의 코루틴") + SupervisorJob() + Dispatchers.Default

    fun runExample() {
        runBlocking(customContext) {
            delayPrintCoroutineContext(customContext)
        }
    }
}

fun main() {
    CoroutineContextExample().runExample()
}

suspend fun delayPrintCoroutineContext(context: CoroutineContext) {
    val job = CoroutineScope(context).launch {
        delay(1000)
        println("Hello from ${coroutineContext[CoroutineName]}")
    }
    job.join()
}