package com.sipe.week5.global.exception

open class CustomException(
	val errorCode: ErrorCode,
	val data: Any? = null,
) : RuntimeException(errorCode.message)
