package com.sipe.week2

class SynchronizedExample {
    private var count = 0 // 공유 자원

    // synchronized 블록을 사용하여 동기화
    @Synchronized
    fun incrementCount() {
        count++
    }

    fun getCount(): Int {
        return count
    }
}

fun main() {
    val example = SynchronizedExample()
    val thread1 = Thread {
        for (i in 0 until 10000) {
            example.incrementCount()
        }
    }
    val thread2 = Thread {
        for (i in 0 until 10000) {
            example.incrementCount()
        }
    }

    // 두 스레드를 시작하고 종료 대기
    thread1.start()
    thread2.start()
    thread1.join()
    thread2.join()

    // 최종 카운트 값을 출력
    println("최종 카운트 값: ${example.getCount()}")
}