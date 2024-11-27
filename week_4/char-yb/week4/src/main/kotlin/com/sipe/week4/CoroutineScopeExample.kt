package com.sipe.week4
import kotlinx.coroutines.*

class CoroutineScopeExample {}

fun main() = runBlocking {
    launchAB()
}

private suspend fun launchAB() = coroutineScope {
    launch {
        println("launch A Start")
        delay(1000L)
        println("launch A End")
    }

    launch {
        println("launch B Start")
        delay(1000L)
        println("launch B End")
    }
    delay(500L)
    println("Hello World!")
}

//class CoroutineScopeExample {
//    // CoroutineScope를 정의
//    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
//
//    fun fetchData(onComplete: (String) -> Unit) {
//        // 스코프 내에서 네트워크 요청 실행
//        scope.launch {
//            try {
//                // 네트워크 호출 시뮬레이션
//                val result = simulateNetworkRequest()
//                // UI 업데이트는 Main 스레드에서
//                withContext(Dispatchers.Main) {
//                    onComplete(result)
//                }
//            } catch (e: Exception) {
//                // 에러 처리
//                withContext(Dispatchers.Main) {
//                    onComplete("Error: ${e.message}")
//                }
//            }
//        }
//    }
//
//    // 네트워크 요청 시뮬레이션
//    private suspend fun simulateNetworkRequest(): String {
//        delay(2000) // 네트워크 대기 시간
//        return "Data from server"
//    }
//
//    // 스코프 종료
//    fun clear() {
//        scope.cancel() // 모든 하위 코루틴 취소
//    }
//}
//
//fun main() = runBlocking {
//    val example = CoroutineScopeExample()
//
//    println("Fetching data...")
//
//    example.fetchData { result ->
//        println("Result: $result")
//    }
//
//    // 프로그램 종료 전 스코프 종료
//    delay(3000)
//    example.clear()
//}

//fun startGlobalTimer() {
//    GlobalScope.launch {
//        while (true) {
//            delay(1000)
//            println("Timer tick at: ${System.currentTimeMillis()}")
//        }
//    }
//}
//
//fun main() {
//    println("Start Timer")
//    startGlobalTimer()
//
//    // 프로그램을 강제로 5초 후 종료
//    Thread.sleep(5000)
//    println("Main function ends")
//}