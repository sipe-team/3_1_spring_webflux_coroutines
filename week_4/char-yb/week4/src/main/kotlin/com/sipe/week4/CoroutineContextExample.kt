package com.sipe.week4

import kotlinx.coroutines.*

class CoroutineContextExample {
}

fun main() {
    // element 조합
    CoroutineName("나만의 코루틴") + SupervisorJob()
    CoroutineName("나만의 코루틴") + Dispatchers.Default

}

suspend fun delayPrintCoroutineContext() {
    val job = CoroutineScope(Dispatchers.Default).launch {
        delay(1000)
        print("Hello")
        coroutineContext.minusKey(CoroutineName.Key)
    }
}