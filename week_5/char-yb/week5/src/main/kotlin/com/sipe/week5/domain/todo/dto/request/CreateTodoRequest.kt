package com.sipe.week5.domain.todo.dto.request

import java.time.LocalDate

data class CreateTodoRequest(
	val title: String,
	val content: String,
	val dueDate: LocalDate,
)
