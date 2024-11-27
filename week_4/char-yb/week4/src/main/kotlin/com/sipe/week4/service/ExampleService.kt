package com.sipe.week4.service

import com.sipe.week4.config.SpringCoroutineDispatcher
import kotlinx.coroutines.*
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service

@Service
class ExampleService(
    private val taskExecutor: ThreadPoolTaskExecutor
) {
    private val springDispatcher = SpringCoroutineDispatcher(taskExecutor)
    private val scope = CoroutineScope(springDispatcher + SupervisorJob())

    fun executeCoroutines() {
        scope.launch {
            logCoroutineStart()
            delay(1000)
            logCoroutineEnd()
        }
    }

    private fun logCoroutineStart() {
        println("Coroutine running on: ${Thread.currentThread().name}")
    }

    private fun logCoroutineEnd() {
        println("Completed on: ${Thread.currentThread().name}")
    }
}