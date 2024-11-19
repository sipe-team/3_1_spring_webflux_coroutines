package com.sipe.week3.application

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AsyncService {

    fun executeAsyncTasks(): String = runBlocking {
        // 각 작업의 시작 시간 기록
        println("Coroutine tasks started at: ${LocalDateTime.now()}")

        // 3개의 I/O 작업을 동시에 실행
        val task1 = async { ioOperation("Task 1") }
        val task2 = async { ioOperation("Task 2") }
        val task3 = async { ioOperation("Task 3") }

        // 모든 작업이 완료된 후 결과를 반환
        val result = "${task1.await()}, ${task2.await()}, ${task3.await()}"

        // 모든 작업이 완료된 시간 기록
        println("Coroutine tasks completed at: ${LocalDateTime.now()}")

        result
    }

    // I/O 작업 시뮬레이션 함수 (비동기적으로 1초 지연)
    private suspend fun ioOperation(taskName: String): String {
        println("$taskName started at: ${LocalDateTime.now()}")
        delay(100) // 실제 I/O 작업을 비동기적으로 처리하는 부분 (예: DB 조회, 외부 API 호출)
        println("$taskName completed at: ${LocalDateTime.now()}")
        return "$taskName completed"
    }
}