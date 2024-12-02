package com.sipe.week5.domain.auth.presentation

import com.sipe.week5.domain.auth.application.AuthService
import com.sipe.week5.domain.auth.dto.TokenPairResponse
import com.sipe.week5.domain.auth.dto.request.SignInRequest
import com.sipe.week5.domain.auth.dto.request.SignUpRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
	private val authService: AuthService,
) {
	@PostMapping("/signIn")
	fun signIn(
		@RequestBody request: @Valid SignInRequest,
	): TokenPairResponse = authService.signIn(request)

	@PostMapping("/signUp")
	suspend fun signUp(
		@RequestBody request: @Valid SignUpRequest,
	): TokenPairResponse = authService.signUp(request)
}
