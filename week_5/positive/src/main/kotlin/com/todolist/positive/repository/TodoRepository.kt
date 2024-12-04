package com.todolist.positive.repository

import Todo
import TodoStatus
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface TodoRepository : ReactiveCrudRepository<Todo, Long> {

    // 상태로 조회하는 기본 메서드
    fun findByStatus(status: TodoStatus): Flux<Todo>

    // Custom 쿼리 예시 (Optional)
    @Query("SELECT * FROM todo WHERE status = :status")
    fun findTodosByStatusCustomQuery(status: String): Flux<Todo>
}
