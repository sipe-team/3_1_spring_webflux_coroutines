package com.sipe.week1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Week1Application

fun main(args: Array<String>) {
	val exThread = ExThread()
	exThread.start()
	println("MyMain Thread Name: ${Thread.currentThread().name}")
}

class ExThread : Thread() {
	override fun run() {
		println("ThreadName: ${Thread.currentThread().name}")
	}
}