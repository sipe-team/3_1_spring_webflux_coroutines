package com.sipe.week5.domain.todo.application

import com.sipe.week5.domain.todo.domain.TodoEntity
import com.sipe.week5.domain.todo.domain.TodoStatus
import com.sipe.week5.domain.todo.dto.request.CreateTodoRequest
import com.sipe.week5.domain.todo.infrastructure.ReactiveTodoRepository
import com.sipe.week5.domain.todo.infrastructure.SuspendableTodoRepository
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import com.sipe.week5.global.util.logging.logger
import com.sipe.week5.global.util.member.MemberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoService(
	private val suspendTodoRepository: SuspendableTodoRepository,
	private val reactiveTodoRepository: ReactiveTodoRepository,
	private val memberUtil: MemberUtil,
) {
	private val log by logger()

	// 생성
	suspend fun createTodo(request: CreateTodoRequest): TodoEntity {
		val todo =
			TodoEntity(
				title = request.title,
				content = request.content,
				dueDate = request.dueDate,
				memberId = memberUtil.getCurrentMember().id,
			)
		return withContext(Dispatchers.IO) { suspendTodoRepository.save(todo) }
	}

	@Transactional(readOnly = true)
	suspend fun findOneTodo(todoId: Long): TodoEntity? =
		runCatching {
			withContext(Dispatchers.IO) {
				suspendTodoRepository.findById(todoId)
			}
		}.onFailure {
			log.error("findOneTodo error: ${it.message}")
			throw CustomException(ErrorCode.TODO_NOT_FOUND)
		}.getOrThrow()

	// 리스트 조회
	@Transactional(readOnly = true)
	suspend fun findListTodo(): List<TodoEntity> =
		withContext(Dispatchers.IO) {
			suspendTodoRepository.findAll().toList()
		}

	// 현재 사용자의 할 일 조회
	@Transactional(readOnly = true)
	suspend fun findByCurrentMemberTodo(): TodoEntity? {
		val currentMember = memberUtil.getCurrentMember()
		return runCatching {
			withContext(Dispatchers.IO) {
				suspendTodoRepository.findByMemberId(currentMember.id)
			}
		}.onFailure {
			log.error("findByCurrentMemberTodo error: ${it.message}")
			throw CustomException(ErrorCode.TODO_NOT_FOUND)
		}.getOrThrow()
	}

	@Transactional(readOnly = true)
	suspend fun findTodoByStatus(status: String): List<TodoEntity> =
		reactiveTodoRepository.findAllByStatus(TodoStatus.valueOf(status)).collectList().awaitFirst()
}
