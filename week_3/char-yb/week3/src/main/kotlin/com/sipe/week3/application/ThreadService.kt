package com.sipe.week3.application

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

@Service
class ThreadService {

    private val executor = Executors.newFixedThreadPool(3) // 3개의 스레드를 사용

    fun executeThreadTasks(): String {
        // 각 작업의 시작 시간 기록
        println("Thread tasks started at: ${LocalDateTime.now()}")

        // 3개의 I/O 작업을 동시에 실행
        val task1: Future<String> = executor.submit(Callable { ioOperation("Task 1") })
        val task2: Future<String> = executor.submit(Callable { ioOperation("Task 2") })
        val task3: Future<String> = executor.submit(Callable { ioOperation("Task 3") })

        // 모든 작업이 완료된 후 결과를 반환
        val result = "${task1.get()}, ${task2.get()}, ${task3.get()}"

        // 모든 작업이 완료된 시간 기록
        println("Thread tasks completed at: ${LocalDateTime.now()}")

        return result
    }

    // I/O 작업 시뮬레이션 함수 (1초 지연)
    private fun ioOperation(taskName: String): String {
        println("$taskName started at: ${LocalDateTime.now()}")
        Thread.sleep(100) // 실제 I/O 작업을 스레드 기반으로 처리하는 부분 (예: DB 조회, 외부 API 호출)
        println("$taskName completed at: ${LocalDateTime.now()}")
        return "$taskName completed"
    }
}