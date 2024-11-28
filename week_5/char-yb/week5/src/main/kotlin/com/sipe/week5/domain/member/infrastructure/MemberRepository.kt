package com.sipe.week5.domain.member.infrastructure

import com.sipe.week5.domain.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
	fun findByLoginId(loginId: String): Member?
}
