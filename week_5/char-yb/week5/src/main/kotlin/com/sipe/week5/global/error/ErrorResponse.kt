package com.sipe.week5.global.error

data class ErrorResponse(val errorClassName: String, val message: String) {
	companion object {
		fun of(
			errorClassName: String,
			message: String,
		): ErrorResponse {
			return ErrorResponse(errorClassName, message)
		}
	}
}
