package com.sipe.week5.domain.auth.dto
import java.security.InvalidParameterException
import java.util.*

enum class TokenType(val value: String) {
	ACCESS("access"),
	REFRESH("refresh"),
	;

	companion object {
		fun from(typeKey: String): TokenType =
			when (typeKey.uppercase(Locale.getDefault())) {
				"ACCESS" -> ACCESS
				"REFRESH" -> REFRESH
				else -> throw InvalidParameterException()
			}
	}
}
