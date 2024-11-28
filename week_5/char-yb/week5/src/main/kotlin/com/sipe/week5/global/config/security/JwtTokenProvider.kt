package com.sipe.week5.global.config.security

import com.sipe.week5.domain.auth.dto.TokenType
import com.sipe.week5.domain.auth.dto.TokenType.*
import com.sipe.week5.domain.auth.exception.AuthenticationExpiredAccessTokenException
import com.sipe.week5.domain.auth.exception.AuthenticationExpiredRefreshTokenException
import com.sipe.week5.domain.auth.exception.AuthenticationInvalidTokenException
import com.sipe.week5.domain.member.domain.Member
import com.sipe.week5.global.common.constants.SecurityConstants.TOKEN_ROLE_NAME
import com.sipe.week5.global.config.properties.JwtProperties
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
	private val jwtProperties: JwtProperties,
) {
	companion object {
		private const val TOKEN_TYPE_KEY_NAME = "type"
		private const val USER_ID_KEY_NAME = "memberId"
	}

	private val refreshTokenKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
	private val accessTokenKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

	private fun createTokenHeader(tokenType: TokenType): Map<String, Any> {
		return mapOf(
			"typ" to "JWT" as Any,
			"alg" to "HS256" as Any,
			"regDate" to System.currentTimeMillis() as Any,
			TOKEN_TYPE_KEY_NAME to tokenType.value as Any,
		)
	}

	fun createAccessToken(member: Member): String =
		Jwts.builder()
			.setHeader(createTokenHeader(ACCESS))
			.setSubject(member.id.toString())
			.claim(TOKEN_ROLE_NAME, member.role.name)
			.claim(USER_ID_KEY_NAME, member.id)
			.setExpiration(Date(System.currentTimeMillis() + jwtProperties.accessTokenExpirationTime * 1000))
			.signWith(accessTokenKey)
			.compact()

	fun createRefreshToken(member: Member): String =
		Jwts.builder()
			.setHeader(createTokenHeader(REFRESH))
			.setSubject(member.id.toString())
			.claim(TOKEN_ROLE_NAME, member.role.name)
			.claim(USER_ID_KEY_NAME, member.id)
			.setExpiration(Date(System.currentTimeMillis() + jwtProperties.accessTokenExpirationTime * 1000))
			.signWith(refreshTokenKey)
			.compact()

	fun validateRefreshToken(refreshToken: String) {
		val claims = getClaims(refreshToken, REFRESH.value, refreshTokenKey)

		if (getTokenType(claims) != REFRESH.value) {
			throw AuthenticationInvalidTokenException()
		}
	}

	fun getMemberIdFromRefreshToken(refreshToken: String): String {
		val claims = getClaims(refreshToken, REFRESH.value, refreshTokenKey)

		if (getTokenType(claims) != ACCESS.value) {
			throw AuthenticationInvalidTokenException()
		}

		return getMemberId(claims)
	}

	fun parseToken(accessToken: String): Authentication {
		val claims: Jws<Claims> = getClaims(accessToken, ACCESS.value, accessTokenKey)

		if (getTokenType(claims) != ACCESS.value) {
			throw AuthenticationInvalidTokenException()
		}

		return UsernamePasswordAuthenticationToken(getMemberId(claims), null, emptyList())
	}

	private fun getClaims(
		token: String,
		tokenType: String,
		key: Key,
	) = runCatching { Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token) }
		.getOrElse {
			when (it) {
				is ExpiredJwtException ->
					when (tokenType) {
						ACCESS.name -> throw AuthenticationExpiredAccessTokenException()
						REFRESH.name -> throw AuthenticationExpiredRefreshTokenException()
						else -> throw AuthenticationInvalidTokenException()
					}

				else -> throw AuthenticationInvalidTokenException()
			}
		}

	private fun getTokenType(claims: Jws<Claims>): String =
		runCatching { claims.header[TOKEN_TYPE_KEY_NAME] as? String? ?: throw AuthenticationInvalidTokenException() }
			.getOrElse { throw AuthenticationInvalidTokenException() }

	private fun getMemberId(claims: Jws<Claims>): String =
		runCatching { claims.body.subject }
			.getOrElse { throw AuthenticationInvalidTokenException() }
}
