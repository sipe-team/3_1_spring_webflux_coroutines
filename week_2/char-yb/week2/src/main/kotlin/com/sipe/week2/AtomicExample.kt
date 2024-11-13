package com.sipe.week2

import java.util.concurrent.atomic.AtomicInteger

class AtomicExample {
    private val count = AtomicInteger(0) // AtomicInteger로 원자성을 보장하는 카운트 변수

    // count를 증가시키는 메서드
    fun increment() {
        count.incrementAndGet() // CAS를 통해 값을 원자적으로 증가
    }

    // 현재 count 값을 반환하는 메서드
    fun getCount(): Int {
        return count.get()
    }
}

fun main() {
    val example = AtomicExample()

    // 두 개의 스레드 생성, 각각 10000번씩 count를 증가시킴
    val thread1 = Thread {
        for (i in 0 until 10000) {
            example.increment()
        }
    }

    val thread2 = Thread {
        for (i in 0 until 10000) {
            example.increment()
        }
    }

    // 스레드 실행
    thread1.start()
    thread2.start()

    // 두 스레드가 작업을 마칠 때까지 대기
    thread1.join()
    thread2.join()

    // 최종 count 값 출력
    println("최종 카운트 값: ${example.getCount()}")
}