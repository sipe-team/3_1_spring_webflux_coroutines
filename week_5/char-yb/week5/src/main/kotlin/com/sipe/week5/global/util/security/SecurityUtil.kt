package com.sipe.week5.global.util.security

import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtil {
	val currentMemberId: Long
		get() {
			val authentication: Authentication = SecurityContextHolder.getContext().authentication
			try {
				return authentication.name.toLong()
			} catch (e: Exception) {
				throw CustomException(ErrorCode.AUTH_NOT_FOUND)
			}
		}
}
