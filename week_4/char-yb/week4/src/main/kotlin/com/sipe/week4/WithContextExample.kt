package com.sipe.week4


import kotlinx.coroutines.*

class WithContextExample {
}

//// suspend 함수로 네트워크 작업 시뮬레이션
//suspend fun fetchData(): String {
//    return withContext(Dispatchers.IO) {  // I/O 스레드에서 실행
//        println("Fetching data on: ${Thread.currentThread().name}")
//        delay(3000)  // 네트워크 지연 시뮬레이션
//        "Data from server"
//    }
//}
//
//fun main() = runBlocking {
//    println("Main program starts: ${Thread.currentThread().name}")
//
//    // suspend 함수 호출
//    val data = fetchData()
//    println("Received data: $data")
//
//    println("Main program ends: ${Thread.currentThread().name}")
//}

// suspend 함수로 데이터를 가져오는 함수
suspend fun fetchDataFromNetwork(): String {
    return withContext(Dispatchers.IO) {
        println("Fetching data on: ${Thread.currentThread().name}")
        delay(3000)  // 네트워크 지연 시뮬레이션
        "Data from network"
    }
}

// suspend 함수로 데이터를 처리하는 함수
suspend fun processData(data: String) {
    withContext(Dispatchers.Default) {
        println("Processing data on: ${Thread.currentThread().name}")
        delay(1000L)  // 데이터 처리 시뮬레이션
        println("Data processed: $data")
    }
}

fun main() = runBlocking {
    println("Main program starts: ${Thread.currentThread().name}")

    // 네트워크에서 데이터를 가져오고 처리
    val data = fetchDataFromNetwork()
    processData(data)

    println("Main program ends: ${Thread.currentThread().name}")
}