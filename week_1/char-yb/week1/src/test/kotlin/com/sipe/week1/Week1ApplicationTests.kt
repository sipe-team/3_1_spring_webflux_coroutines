package com.sipe.week1

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Week1ApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun threadStart() {
		val thread = MyThread()

		thread.start()
	}

	class MyThread : Thread() {
		override fun run() {
			println("Thread is running")
		}
	}
}
