package com.sipe.week5.domain.auth.dto.request


data class SignUpRequest(
	val loginId: String,
	val password: String,
	val username: String,
)
