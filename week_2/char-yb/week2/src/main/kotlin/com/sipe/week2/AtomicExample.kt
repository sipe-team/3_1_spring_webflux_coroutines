package com.sipe.week2

import java.util.concurrent.atomic.AtomicInteger

class AtomicExample {
    private var count = 0 // 공유 자원

    fun incrementCount() {
        for (i in 0 until 10000) {
            count++
        }
    }

    fun getCount(): Int {
        val test = AtomicInteger(0)
        return count
    }
}