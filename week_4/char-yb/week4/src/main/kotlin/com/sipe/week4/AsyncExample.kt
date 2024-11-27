package com.sipe.week4

import kotlinx.coroutines.*

class AsyncExample {
}

//fun main() = runBlocking {
//    val deferredInt: Deferred<Int> = async {
//        1 // 마지막 줄 반환
//    }
//    val value = deferredInt.await()
//    println(value) // 1 출력
//}

fun main() {
    val deferred = CoroutineScope(Dispatchers.IO).async {
        1
    }
    deferred.await()
} 