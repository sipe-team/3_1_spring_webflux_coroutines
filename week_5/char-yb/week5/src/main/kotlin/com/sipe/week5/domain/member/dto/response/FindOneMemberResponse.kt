package com.sipe.week5.domain.member.dto.response

import com.sipe.week5.domain.member.domain.Member
import com.sipe.week5.domain.member.domain.MemberRole

data class FindOneMemberResponse(
	val id: Long,
	val username: String,
	val loginId: String,
	val studyGoal: String,
	val role: MemberRole,
) {
	companion object {
		fun from(member: Member) =
			FindOneMemberResponse(
				id = member.id,
				username = member.username,
				loginId = member.loginId,
				studyGoal = member.studyGoal ?: "없음",
				role = member.role,
			)
	}
}
