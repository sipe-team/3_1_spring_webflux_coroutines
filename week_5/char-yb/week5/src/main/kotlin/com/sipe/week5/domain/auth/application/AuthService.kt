package com.sipe.week5.domain.auth.application

import com.sipe.week5.domain.auth.dto.TokenPairResponse
import com.sipe.week5.domain.auth.dto.request.SignInRequest
import com.sipe.week5.domain.auth.dto.request.SignUpRequest
import com.sipe.week5.domain.member.domain.Member
import com.sipe.week5.domain.member.infrastructure.ReactiveMemberRepository
import com.sipe.week5.domain.member.infrastructure.SuspendableMemberRepository
import com.sipe.week5.global.config.security.JwtTokenProvider
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
	private val suspendMemberRepository: SuspendableMemberRepository,
	private val reactiveMemberRepository: ReactiveMemberRepository,
	private val passwordEncoder: PasswordEncoder,
	private val jwtTokenProvider: JwtTokenProvider,
) {
	fun signIn(signInRequest: SignInRequest): TokenPairResponse {
		val findMember =
			suspendMemberRepository.findByLoginId(signInRequest.loginId)
				?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

		if (!passwordEncoder.matches(signInRequest.password, findMember.password)) {
			throw CustomException(ErrorCode.PASSWORD_NOT_MATCHES)
		}

		return getLoginResponse(findMember)
	}

	suspend fun signUp(signUpRequest: SignUpRequest): TokenPairResponse {
		suspendMemberRepository.findByLoginId(signUpRequest.loginId) ?: throw CustomException(ErrorCode.MEMBER_ALREADY_REGISTERED)

		val saveMember: Member = suspendMemberRepository.save(Member(
			loginId = signUpRequest.loginId,
			password = passwordEncoder.encode(signUpRequest.password),
			username = signUpRequest.username,
			studyGoal = signUpRequest.studyGoal,
		))

		return getLoginResponse(saveMember)
	}

	private fun getLoginResponse(member: Member): TokenPairResponse {
		val accessToken: String = jwtTokenProvider.createAccessToken(member)
		val refreshToken: String = jwtTokenProvider.createRefreshToken(member)

		return TokenPairResponse.of(accessToken, refreshToken)
	}
}
