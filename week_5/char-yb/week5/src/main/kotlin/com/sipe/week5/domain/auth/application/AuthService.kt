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
import kotlinx.coroutines.reactor.awaitSingle
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
	suspend fun signIn(signInRequest: SignInRequest): TokenPairResponse {
		val findMember =
			suspendMemberRepository.findByLoginId(signInRequest.loginId)
				?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

//		val findMemberReactive =
//			reactiveMemberRepository.findByLoginId(signInRequest.loginId)?.awaitSingle()
//				?: throw CustomException(ErrorCode.MEMBER_NOT_FOUND)

		if (!passwordEncoder.matches(signInRequest.password, findMember.password)) {
			throw CustomException(ErrorCode.PASSWORD_NOT_MATCHES)
		}

		return getLoginResponse(findMember)
	}

	suspend fun signUp(signUpRequest: SignUpRequest): TokenPairResponse {
		suspendMemberRepository.findByLoginId(signUpRequest.loginId)?.let {
			throw CustomException(ErrorCode.MEMBER_ALREADY_REGISTERED)
		}

//		reactiveMemberRepository.findByLoginId(signUpRequest.loginId)?.awaitSingle()?.let {
//			throw CustomException(ErrorCode.MEMBER_ALREADY_REGISTERED)
//		}

		val saveMember = suspendMemberRepository.save(Member(
			loginId = signUpRequest.loginId,
			password = passwordEncoder.encode(signUpRequest.password),
			username = signUpRequest.username,
		))

		return getLoginResponse(saveMember)
	}

	private fun getLoginResponse(member: Member): TokenPairResponse {
		val accessToken: String = jwtTokenProvider.createAccessToken(member)
		val refreshToken: String = jwtTokenProvider.createRefreshToken(member)

		return TokenPairResponse.of(accessToken, refreshToken)
	}
}
