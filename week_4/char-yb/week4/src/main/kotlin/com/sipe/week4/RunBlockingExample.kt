package com.sipe.week4
import kotlinx.coroutines.*

class RunBlockingExample {
}

fun main() = runBlocking {
    println("Start") // 즉시 실행
    delay(5000) // 5초 대기 (코루틴)
    println("End") // 5초 후 실행
}