package com.sipe.week2

import java.util.*

class Problem1 {

    companion object {
        private var count: Int = 0
        @JvmStatic
        fun main(args: Array<String>) {
            for (i in 0..99) {
                Thread {
                    for (j in 0..999) println(count++)
                }.start()
            }
        }
    }
}