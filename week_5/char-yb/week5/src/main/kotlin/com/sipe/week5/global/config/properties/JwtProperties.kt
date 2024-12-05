package com.sipe.week5.global.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
	val accessTokenSecret: String,
	val refreshTokenSecret: String,
	val accessTokenExpirationTime: Long,
	val refreshTokenExpirationTime: Long,
) {
	fun accessTokenExpirationMilliTime(): Long {
		return accessTokenExpirationTime * 1000
	}

	fun refreshTokenExpirationMilliTime(): Long {
		return refreshTokenExpirationTime * 1000
	}
}
