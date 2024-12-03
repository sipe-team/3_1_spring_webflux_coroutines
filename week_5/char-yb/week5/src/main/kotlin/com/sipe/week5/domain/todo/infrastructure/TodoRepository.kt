package com.sipe.week5.domain.todo.infrastructure

import com.sipe.week5.domain.todo.domain.TodoEntity
import com.sipe.week5.domain.todo.domain.TodoStatus
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Repository
interface SuspendableTodoRepository : CoroutineCrudRepository<TodoEntity, Long> {
	suspend fun findByMemberId(memberId: Long): TodoEntity?
	suspend fun findAllByStatus(status: TodoStatus): List<TodoEntity>
}

@Repository
interface ReactiveTodoRepository : R2dbcRepository<TodoEntity, Long> {
	fun findByMemberId(memberId: Long): Mono<TodoEntity>?
	fun findAllByStatus(status: TodoStatus): Flux<TodoEntity>
}
