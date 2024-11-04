package com.sipe.week1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Week1Application

fun main(args: Array<String>) {
	// 스레드 생성
	val exThread = ExThread()

	// start를 통해 run 메서드가 실행되는데, run을 직접 실행하는게 아닌 start를 실행하는 것이다.
	exThread.start()
	println("MyMain Thread Name: ${Thread.currentThread().name}")
}

class ExThread : Thread() {
	override fun run() {
		println("ThreadName: ${currentThread().name}")
	}
}