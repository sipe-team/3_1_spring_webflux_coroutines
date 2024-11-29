package com.sipe.week5.domain.todo.presentation

import com.sipe.week5.domain.todo.application.TodoService
import com.sipe.week5.domain.todo.domain.TodoEntity
import com.sipe.week5.domain.todo.dto.request.CreateTodoRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todo")
class TodoController (
	private val todoService: TodoService
) {
	@PostMapping
	fun createTodo(
		@RequestBody request: CreateTodoRequest
	): TodoEntity {
		return todoService.createTodo(request)
	}

	@GetMapping
	fun findListTodo(): List<TodoEntity> {
		return todoService.findListTodo()
	}

	@GetMapping("/{todoId}")
	fun findOneTodo(
		@PathVariable todoId: Long
	): TodoEntity {
		return todoService.findOneTodo(todoId)
	}

	@GetMapping("/me")
	fun findByCurrentMemberTodo() {
		todoService.findByCurrentMemberTodo()
	}

}
