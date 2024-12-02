package com.sipe.week5.domain.member.application

import com.sipe.week5.domain.member.dto.response.FindOneMemberResponse
import com.sipe.week5.domain.member.infrastructure.SuspendableMemberRepository
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import com.sipe.week5.global.util.member.MemberUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: SuspendableMemberRepository,
    private val memberUtil: MemberUtil,
) {
	@Transactional(readOnly = true)
	suspend fun findMemberMe(): FindOneMemberResponse {
		val findMember =
			memberRepository.findById(memberUtil.getCurrentMember().id)
				?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)
		return FindOneMemberResponse.from(findMember)
	}
}
