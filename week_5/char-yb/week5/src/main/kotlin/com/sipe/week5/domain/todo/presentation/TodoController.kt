package com.sipe.week5.domain.todo.presentation

import com.sipe.week5.domain.todo.application.TodoService
import com.sipe.week5.domain.todo.domain.TodoEntity
import com.sipe.week5.domain.todo.dto.request.CreateTodoRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todo")
class TodoController(
	private val todoService: TodoService,
) {
	@PostMapping
	suspend fun createTodo(
		@RequestBody request: CreateTodoRequest,
	): TodoEntity = todoService.createTodo(request)

	@GetMapping
	suspend fun findListTodo(): List<TodoEntity> = todoService.findListTodo()

	@GetMapping("/{todoId}")
	suspend fun findOneTodo(
		@PathVariable todoId: Long,
	): TodoEntity? = todoService.findOneTodo(todoId)

	@GetMapping("/search")
	suspend fun findTodoByStatus(
		@RequestParam status: String,
	): List<TodoEntity> = todoService.findTodoByStatus(status)

	@GetMapping("/me")
	suspend fun findByCurrentMemberTodo(): TodoEntity? = todoService.findByCurrentMemberTodo()
}
