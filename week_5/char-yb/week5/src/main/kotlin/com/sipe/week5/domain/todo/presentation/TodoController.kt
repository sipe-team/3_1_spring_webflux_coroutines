package com.sipe.week5.domain.todo.presentation

import com.sipe.week5.domain.todo.application.TodoService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/todo")
class TodoController (
	private val todoService: TodoService
) {
}
