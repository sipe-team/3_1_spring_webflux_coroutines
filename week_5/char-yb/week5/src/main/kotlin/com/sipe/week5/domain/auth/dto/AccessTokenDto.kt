package com.sipe.week5.domain.auth.dto

import com.sipe.week5.domain.member.domain.MemberRole

data class AccessTokenDto(val memberId: Long, val memberRole: MemberRole, val tokenValue: String)
