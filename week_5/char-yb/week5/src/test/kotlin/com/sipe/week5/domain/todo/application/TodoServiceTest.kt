package com.sipe.week5.domain.todo.application

import com.sipe.week5.domain.member.domain.Member
import com.sipe.week5.domain.member.domain.MemberRole
import com.sipe.week5.domain.member.infrastructure.ReactiveMemberRepository
import com.sipe.week5.domain.member.infrastructure.SuspendableMemberRepository
import com.sipe.week5.domain.todo.domain.TodoEntity
import com.sipe.week5.domain.todo.domain.TodoStatus
import com.sipe.week5.domain.todo.infrastructure.ReactiveTodoRepository
import com.sipe.week5.domain.todo.infrastructure.SuspendableTodoRepository
import com.sipe.week5.global.config.security.PrincipalDetails
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import java.time.LocalDate
import kotlin.system.measureTimeMillis

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class TodoServiceTest
	@Autowired
	constructor(
		private val todoService: TodoService,
		private val suspendableMemberRepository: SuspendableMemberRepository,
		private val reactiveMemberRepository: ReactiveMemberRepository,
		private val suspendableTodoRepository: SuspendableTodoRepository,
		private val reactiveTodoRepository: ReactiveTodoRepository,
		private val passwordEncoder: PasswordEncoder,
	) {

	private lateinit var testMember: Member
	private var todos = mutableListOf<TodoEntity>()

	@BeforeEach
	fun setUp() {
		runBlocking {
			testMember = suspendableMemberRepository.save(
				Member(
					loginId = "test",
					password = passwordEncoder.encode("test"),
					username = "test",
					role = MemberRole.USER,
				)
			)

			for (i in 1..50_000_000) {
				todos.add(
					TodoEntity(
						title = "test $i",
						content = "test $i",
						dueDate = LocalDate.now(),
						memberId = testMember.id,
					)
				)
			}
			suspendableTodoRepository.saveAll(todos)

			val principalDetails = PrincipalDetails(testMember.id, testMember.role)
			val authentication: Authentication =
				UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.authorities)
			SecurityContextHolder.getContext().authentication = authentication
		}
	}

	@Test
	fun `test performance of suspendableTodoRepository for findTodoByStatus`() = runBlocking {
		val fetchTime = measureTimeMillis {
			suspendableTodoRepository.findAllByStatus(TodoStatus.TODO).toList()
		}

		println("Time taken to fetch records using suspendableTodoRepository: $fetchTime ms")
	}

	@Test
	fun `test performance of reactiveTodoRepository for findTodoByStatus`() {
		val fetchTime = measureTimeMillis {
			reactiveTodoRepository.findAllByStatus(TodoStatus.TODO).collectList().block()
		}

		println("Time taken to fetch records using reactiveTodoRepository: $fetchTime ms")
	}


}
