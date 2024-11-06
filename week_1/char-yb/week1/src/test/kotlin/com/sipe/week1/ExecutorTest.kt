package com.sipe.week1

import org.junit.jupiter.api.Test
import java.util.concurrent.Executor

class ExecutorTest {

    @Test
    fun executorRun() {
        val runnable = Runnable {
            // Thread: Test worker
            println("Thread: ${Thread.currentThread().name}")
        }

        val executor: Executor = RunExecutor()
        executor.execute(runnable)
    }

    class RunExecutor : Executor {
        override fun execute(command: Runnable) {
            command.run()
        }
    }

    @Test
    fun executorStart() {
        val runnable =
            Runnable { println("Thread: " + Thread.currentThread().name) }

        val executor: Executor = StartExecutor()
        executor.execute(runnable)
    }

    class StartExecutor : Executor {
        override fun execute(command: Runnable) {
            Thread(command).start()
        }
    }

}