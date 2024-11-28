package com.sipe.week5.domain.todo.application

import com.sipe.week5.domain.todo.infrastructure.TodoRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class TodoService (
	private val todoRepository: TodoRepository
) {
}
