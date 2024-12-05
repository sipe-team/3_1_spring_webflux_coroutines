package com.sipe.week5.domain.auth.exception

import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode

open class AuthenticationException : CustomException {
	constructor(errorCode: ErrorCode) : super(errorCode)
	constructor(errorCode: ErrorCode, data: Any? = null) : super(errorCode, data)
}

class AuthenticationInvalidTokenException : AuthenticationException(ErrorCode.INVALID_AUTH_TOKEN)

class AuthenticationExpiredAccessTokenException : AuthenticationException(ErrorCode.EXPIRED_ACCESS_TOKEN)

class AuthenticationExpiredRefreshTokenException : AuthenticationException(ErrorCode.EXPIRED_REFRESH_TOKEN)

class AuthenticationTokenNotExistException : AuthenticationException(ErrorCode.NOT_EXIST_TOKEN)
