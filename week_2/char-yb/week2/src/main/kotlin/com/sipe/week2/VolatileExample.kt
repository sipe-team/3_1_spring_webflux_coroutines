package com.sipe.week2

class VolatileExample {
    @Volatile
    var count: Int = 0

    private fun incrementCount() {
        for (i in 0..9999) {
            count++
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val instance = VolatileExample()

            // Create threads
            val thread1 = Thread { instance.incrementCount() }
            val thread2 = Thread { instance.incrementCount() }

            // Start threads
            thread1.start()
            thread2.start()

            // Wait for threads to finish
            thread1.join()
            thread2.join()

            // Print final count value
            println("Final count value: ${instance.count}")
        }
    }
}