package com.sipe.week5.domain.todo.application

import com.sipe.week5.domain.todo.domain.TodoEntity
import com.sipe.week5.domain.todo.dto.request.CreateTodoRequest
import com.sipe.week5.domain.todo.infrastructure.ReactiveTodoRepository
import com.sipe.week5.domain.todo.infrastructure.SuspendableTodoRepository
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import com.sipe.week5.global.util.member.MemberUtil
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoService (
	private val suspendTodoRepository: SuspendableTodoRepository,
	private val reactiveTodoRepository: ReactiveTodoRepository,
	private val memberUtil: MemberUtil
) {
	// 생성
	suspend fun createTodo(request: CreateTodoRequest): TodoEntity {
		val todo = TodoEntity(
			title = request.title,
			content = request.content,
			dueDate = request.dueDate,
			memberId = memberUtil.getCurrentMember().id
		)
		return suspendTodoRepository.save(todo)
	}

	@Transactional(readOnly = true)
	suspend fun findOneTodo(todoId: Long): TodoEntity {
		return suspendTodoRepository.findById(todoId) ?: throw CustomException(ErrorCode.TODO_NOT_FOUND)
	}


	// 리스트 조회
	@Transactional(readOnly = true)
	suspend fun findListTodo(): List<TodoEntity> {
		return reactiveTodoRepository.findAll().collectList().awaitFirst()
	}

	// 현재 사용자의 할 일 조회
	@Transactional(readOnly = true)
	suspend fun findByCurrentMemberTodo() : TodoEntity {
		val currentMember = memberUtil.getCurrentMember()
		return suspendTodoRepository.findByMemberId(currentMember.id)
	}
}
