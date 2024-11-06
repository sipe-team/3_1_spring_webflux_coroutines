package com.sipe.week1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors


class FutureTest {

    @Test
    @Throws(ExecutionException::class, InterruptedException::class)
    fun get() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = callable()

        // It takes 3 seconds by blocking(블로킹에 의해 3초 걸림)
        val future = executorService.submit(callable)

        println(future.get())

        executorService.shutdown()
    }

    @Test
    fun isCancelled_False() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = callable()

        val future = executorService.submit(callable)
        assertThat(future.isCancelled).isFalse()

        executorService.shutdown()
    }

    @Test
    fun isCancelled_True() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = callable()

        val future = executorService.submit(callable)
        future.cancel(true)

        assertThat(future.isCancelled).isTrue()
        executorService.shutdown()
    }

    @Test
    fun isDone_False() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = callable()

        val future = executorService.submit(callable)

        assertThat(future.isDone).isFalse()
        executorService.shutdown()
    }

    @Test
    fun isDone_True() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = callable()

        val future = executorService.submit(callable)

        while (future.isDone) {
            assertThat(future.isDone).isTrue()
            executorService.shutdown()
        }
    }

    private fun callable(): Callable<String> = Callable {
        Thread.sleep(3000L)
        "Thread: " + Thread.currentThread().name
    }
}