package com.todolist.positive.controller

import Todo
import TodoStatus
import com.todolist.positive.service.TodoService
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import kotlinx.coroutines.flow.*

@RestController
@RequestMapping("/todos")
class TodoController(private val todoService: TodoService) {

    // Todo 생성 API
    @PostMapping
    suspend fun createTodo(@RequestBody todo: Todo): Todo? {
        return todoService.createTodo(todo)
    }

    // Todo 단건 조회 API
    @GetMapping("/{id}")
    suspend fun getTodoById(@PathVariable id: Long): Todo? {
        return todoService.getTodoById(id)
    }

    // 상태별 Todo 조회 API
    @GetMapping
    suspend fun getTodosByStatus(@RequestParam status: TodoStatus): ResponseEntity<Flow<Todo>> {
        val todos = todoService.getTodosByStatus(status)
        return ResponseEntity.ok(todos)
    }

    // 전체 Todo 조회 API
    @GetMapping("/all")
    suspend fun getAllTodos(): ResponseEntity<Flow<Todo>> {
        val todos = todoService.getAllTodos()
        return ResponseEntity.ok(todos)
    }
}
