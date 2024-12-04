package com.todolist.positive.service

import Todo
import TodoStatus
import com.todolist.positive.repository.TodoRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Service
@Service
class TodoService(private val todoRepository: TodoRepository) {

    suspend fun createTodo(todo: Todo): Todo {
        // ID가 null이어야 새로 생성
        if (todo.id != null) {
            throw IllegalArgumentException("ID should not be provided for new Todo creation.")
        }
        return todoRepository.save(todo).awaitFirst() // save 결과 반환
    }

    suspend fun getTodoById(id: Long): Todo? {
        return todoRepository.findById(id).awaitFirstOrNull()
    }

    fun getTodosByStatus(status: TodoStatus): Flow<Todo> {
        return todoRepository.findByStatus(status).asFlow()
    }

    suspend fun getAllTodos(): Flow<Todo> {
        return todoRepository.findAll().asFlow()
    }
}
