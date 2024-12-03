package com.sipe.week5.domain.member.application

import com.sipe.week5.domain.member.dto.response.FindOneMemberResponse
import com.sipe.week5.domain.member.infrastructure.SuspendableMemberRepository
import com.sipe.week5.global.util.member.MemberUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: SuspendableMemberRepository,
    private val memberUtil: MemberUtil,
) {
	@Transactional(readOnly = true)
	suspend fun findMemberMe(): FindOneMemberResponse =
		FindOneMemberResponse.from(memberUtil.getCurrentMember())
}
