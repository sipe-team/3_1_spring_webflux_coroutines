package com.sipe.week5.domain.todo.application

import com.sipe.week5.domain.todo.domain.TodoEntity
import com.sipe.week5.domain.todo.dto.request.CreateTodoRequest
import com.sipe.week5.domain.todo.infrastructure.TodoRepository
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import com.sipe.week5.global.util.member.MemberUtil
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class TodoService (
	private val todoRepository: TodoRepository,
	private val memberUtil: MemberUtil
) {
	// 생성
	fun createTodo(request: CreateTodoRequest): TodoEntity {
		val todo = TodoEntity(
			title = request.title,
			content = request.content,
			dueDate = request.dueDate,
			member = memberUtil.currentMember
		)
		return todoRepository.save(todo)
	}

	// After
	fun findOneTodo(todoId: Long): TodoEntity {
		return todoRepository.findById(todoId).orElseThrow { CustomException(ErrorCode.TODO_NOT_FOUND) }
	}

	// 리스트 조회
	fun findListTodo(): List<TodoEntity> {
		return todoRepository.findAll()
	}

	// 현재 사용자의 할 일 조회
	fun findByCurrentMemberTodo() {
		val currentMember = memberUtil.currentMember
		todoRepository.findByMemberId(currentMember.id)
	}
}
