package com.sipe.week5.domain.member.application

import com.sipe.week5.domain.member.dto.response.FindOneMemberResponse
import com.sipe.week5.domain.member.infrastructure.MemberRepository
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import com.sipe.week5.global.util.MemberUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
	private val memberRepository: MemberRepository,
	private val memberUtil: MemberUtil,
) {
	@Transactional(readOnly = true)
	fun findMemberMe(): FindOneMemberResponse {
		val findMember =
			memberRepository.findById(memberUtil.currentMember.id)
				.orElseThrow { CustomException(ErrorCode.MEMBER_NOT_FOUND) }
		return FindOneMemberResponse.from(findMember)
	}
}
