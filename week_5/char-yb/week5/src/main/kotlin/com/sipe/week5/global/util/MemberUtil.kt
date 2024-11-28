package com.sipe.week5.global.util

import com.sipe.week5.domain.member.domain.Member
import com.sipe.week5.domain.member.infrastructure.MemberRepository
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import com.sipe.week5.global.util.security.SecurityUtil
import org.springframework.stereotype.Component

@Component
class MemberUtil(
	private val securityUtil: SecurityUtil,
	private val memberRepository: MemberRepository,
) {
	val currentMember: Member
		get() =
			memberRepository
				.findById(securityUtil.currentMemberId)
				.orElseThrow { CustomException(ErrorCode.MEMBER_NOT_FOUND) }

	fun getMemberByMemberId(memberId: Long): Member {
		return memberRepository
			.findById(memberId)
			.orElseThrow { CustomException(ErrorCode.MEMBER_NOT_FOUND) }
	}
}
