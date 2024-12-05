package com.sipe.week5.domain.auth.dto

data class RefreshTokenDto(val memberId: Long, val tokenValue: String, val ttl: Long)
