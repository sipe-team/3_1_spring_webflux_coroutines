package com.sipe.week4

import kotlinx.coroutines.*

class LaunchExample {
}

fun main() = runBlocking {
    println("Start")

    launch {
        delay(1000) // 1초 대기
        println("Task 1 completed")
    }

    launch {
        delay(500) // 0.5초 대기
        println("Task 2 completed")
    }

    println("End")
    val job: Job = launch { println(1) }
    job.join()
    println(2)
}