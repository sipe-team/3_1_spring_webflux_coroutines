package com.sipe.week5.global.util.member

import com.sipe.week5.domain.member.domain.Member
import com.sipe.week5.domain.member.infrastructure.SuspendableMemberRepository
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import com.sipe.week5.global.util.security.SecurityUtil
import org.springframework.stereotype.Component

@Component
class MemberUtil(
	private val securityUtil: SecurityUtil,
	private val suspendableMemberRepository: SuspendableMemberRepository
) {
	suspend fun getCurrentMember(): Member = suspendableMemberRepository
			.findById(securityUtil.currentMemberId) ?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

	suspend fun getMemberByMemberId(memberId: Long): Member = suspendableMemberRepository
			.findById(memberId) ?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

}
