package com.sipe.week5.domain.todo.infrastructure

import com.sipe.week5.domain.todo.domain.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long>
