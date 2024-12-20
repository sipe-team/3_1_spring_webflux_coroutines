package com.sipe.week3

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoRoutineExample1 {
}

fun main() = runBlocking { // this: CoroutineScope
    helloWorld()
}

suspend fun helloWorld() = coroutineScope {
    launch { // launch a new coroutine and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }
    println("Hello") // main coroutine continues while a previous one is delayed
}