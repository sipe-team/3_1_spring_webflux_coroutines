package com.sipe.week5.global.exception
import org.springframework.http.HttpStatus

enum class ErrorCode(
	val status: HttpStatus,
	val message: String,
) {
	// Common
	METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 한 값 타입이 잘못되어 binding에 실패하였습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP method 입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다."),

	// Member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),

	// Security
	AUTH_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보를 찾을 수 없습니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT Access 토큰입니다."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT Refresh 토큰입니다."),
	NOT_EXIST_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
	MEMBER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 가입된 회원입니다."),
	PASSWORD_NOT_MATCHES(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 검증에 실패했습니다."),

	// Todo
	TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Todo를 찾을 수 없습니다."),
	TODO_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 완료된 Todo입니다."),
	TODO_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 Todo입니다."),
	TODO_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 Todo입니다."),
}
