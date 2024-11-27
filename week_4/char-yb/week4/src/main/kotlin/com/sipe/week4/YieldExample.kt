package com.sipe.week4

import kotlinx.coroutines.*

class YieldExample {
}

suspend fun exampleYield() {
    repeat(5) {
        println("Working...")
        yield() // 다른 코루틴에게 실행 권한 양보
    }
}

fun main(): Unit = runBlocking {
    launch {
        exampleYield()
    }

    launch {
        repeat(5) {
            println("Other task...")
            delay(200L)
        }
    }
}