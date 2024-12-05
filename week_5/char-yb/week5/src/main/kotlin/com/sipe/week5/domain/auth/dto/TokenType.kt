package com.sipe.week5.domain.auth.dto
import java.util.*

enum class TokenType(val value: String) {
	ACCESS("access"),
	REFRESH("refresh"),
	;

	companion object {
		fun from(typeKey: String): TokenType =
			typeKey.takeIf { it.isNotBlank() }
				?.uppercase(Locale.ENGLISH)
				?.let { key ->
					when (key) {
						"ACCESS" -> ACCESS
						"REFRESH" -> REFRESH
						else -> throw IllegalArgumentException("Invalid token type: $typeKey")
					}
				} ?: throw IllegalArgumentException("Token type cannot be null or empty")
	}
}
