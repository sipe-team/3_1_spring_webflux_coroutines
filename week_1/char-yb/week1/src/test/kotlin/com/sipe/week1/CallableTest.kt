package com.sipe.week1

import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class CallableTest {

    @Test
    fun callable_void() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = Callable<Void> {
            // Thread: pool-1-thread-1 is running
            println("Thread: ${Thread.currentThread().name} is running")
            null
        }

        executorService.submit(callable)
        executorService.shutdown()
    }

    @Test
    fun callable_String() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = Callable { "Thread: " + Thread.currentThread().name }

        executorService.submit(callable)
        executorService.shutdown()
    }

}