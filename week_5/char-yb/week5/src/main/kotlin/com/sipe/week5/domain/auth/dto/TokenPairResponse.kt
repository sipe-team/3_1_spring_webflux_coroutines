package com.sipe.week5.domain.auth.dto


data class TokenPairResponse(
	val accessToken: String,
	val refreshToken: String,
) {
	companion object {
		fun of(
			accessToken: String,
			refreshToken: String,
		) = TokenPairResponse(accessToken, refreshToken)
	}
}
