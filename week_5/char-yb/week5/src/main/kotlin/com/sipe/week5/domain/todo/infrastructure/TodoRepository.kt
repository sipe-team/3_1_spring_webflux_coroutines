package com.sipe.week5.domain.todo.infrastructure

import com.sipe.week5.domain.todo.domain.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<TodoEntity, Long> {
	fun findByMemberId(memberId: Long): List<TodoEntity>
}
