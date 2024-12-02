package com.sipe.week5.domain.member.infrastructure

import com.sipe.week5.domain.member.domain.Member
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface SuspendableMemberRepository : CoroutineCrudRepository<Member, Long> {
	fun findByLoginId(loginId: String): Member?
}

@Repository
interface ReactiveMemberRepository : R2dbcRepository<Member, Long> {
	fun findByLoginId(loginId: String): Mono<Member>?
}
