package com.sipe.week5.domain.auth.dto.request

data class SignInRequest(
	val loginId: String,
	val password: String,
)
