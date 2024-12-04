package com.todolist.positive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PositiveApplication

fun main(args: Array<String>) {
	runApplication<PositiveApplication>(*args)
}
