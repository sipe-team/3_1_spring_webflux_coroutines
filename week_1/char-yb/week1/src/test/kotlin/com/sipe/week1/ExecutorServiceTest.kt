package com.sipe.week1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.*


class ExecutorServiceTest {

    // 라이프사이클 관리
    @Test
    fun shutdown() {
        val executorService: ExecutorService = Executors.newFixedThreadPool(10)

        val runnable =
            Runnable { println("Thread: " + Thread.currentThread().name) }
        executorService.execute(runnable)

        // shutdown이 호출되기 전까지 계속해서 다음 작업이 대기되는데, 작업이 완료되었다면 shutdown을 명시적 호출해야 한다.
        // 만약 작업 실행 후에 shtudown을 해주지 않으면 다음과 같이 프로세스가 끝나지 않고, 계속해서 다음 작업을 기다리게 된다
        executorService.shutdown()

        val result: RejectedExecutionException = assertThrows(RejectedExecutionException::class.java) {
            executorService.execute(
                runnable
            )
        }
        assertThat(result).isInstanceOf(RejectedExecutionException::class.java)
    }

    @Test
    @Throws(InterruptedException::class)
    // shutdownNow()는 현재 진행 중인 작업을 취소하고 대기 중인 작업을 무시한다.
    // interrupt 여부에 따른 처리 코드가 존재하지 않다면 계속 실행되므로 interrupt 여부를 확인하여 종료해야 한다.
    fun shutdownNow() {
        val runnable = Runnable {
            println("Start")
            while (true) {
                if (Thread.currentThread().isInterrupted) {
                    break
                }
            }
            println("End")
        }

        val executorService: ExecutorService = Executors.newFixedThreadPool(10)
        executorService.execute(runnable)

        executorService.shutdownNow()
        Thread.sleep(1000L)
    }

    // 비동기 작업
    @Test
    @Throws(InterruptedException::class, ExecutionException::class)
    fun invokeAll() {
        val executorService: ExecutorService = Executors.newFixedThreadPool(10)
        val start = Instant.now()

        val hello = Callable {
            Thread.sleep(1000L)
            val result = "Hello"
            println("result = $result")
            result
        }

        val mang = Callable {
            Thread.sleep(2000L)
            val result = "Mang"
            println("result = $result")
            result
        }

        val kyu = Callable {
            Thread.sleep(3000L)
            val result = "kyu"
            println("result = $result")
            result
        }

        val futures: List<Future<String?>> = executorService.invokeAll(Arrays.asList(hello, mang, kyu))
        for (f in futures) {
            println(f.get())
        }

        System.out.println("time = " + Duration.between(start, Instant.now()).getSeconds())
        executorService.shutdown()
    }

    @Test
    @Throws(InterruptedException::class, ExecutionException::class)
    fun invokeAny() {
        val executorService: ExecutorService = Executors.newFixedThreadPool(10)
        val start = Instant.now()

        val hello = Callable {
            Thread.sleep(1000L)
            val result = "Hello"
            println("result = $result")
            result
        }

        val mang = Callable {
            Thread.sleep(2000L)
            val result = "Mang"
            println("result = $result")
            result
        }

        val kyu = Callable {
            Thread.sleep(3000L)
            val result = "kyu"
            println("result = $result")
            result
        }

        val result: String = executorService.invokeAny(listOf(hello, mang, kyu))
        println("result = " + result + " time = " + Duration.between(start, Instant.now()).seconds)

        executorService.shutdown()
    }
}