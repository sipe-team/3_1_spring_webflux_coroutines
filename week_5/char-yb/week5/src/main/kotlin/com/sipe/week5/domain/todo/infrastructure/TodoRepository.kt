package com.sipe.week5.domain.todo.infrastructure

import com.sipe.week5.domain.todo.domain.TodoEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface SuspendableTodoRepository : CoroutineCrudRepository<TodoEntity, Long> {
	fun findByMemberId(memberId: Long): TodoEntity
}

@Repository
interface ReactiveTodoRepository : R2dbcRepository<TodoEntity, Long>
