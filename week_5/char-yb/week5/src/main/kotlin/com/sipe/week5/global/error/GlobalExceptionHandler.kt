package com.sipe.week5.global.error
import com.sipe.week5.global.common.response.GlobalResponse
import com.sipe.week5.global.exception.CustomException
import com.sipe.week5.global.exception.ErrorCode
import com.sipe.week5.global.util.logging.logger
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.function.Consumer

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
	private val log by logger()

	override fun handleExceptionInternal(
		ex: Exception,
		body: Any?,
		headers: HttpHeaders,
		statusCode: HttpStatusCode,
		request: WebRequest,
	): ResponseEntity<Any>? {
		val errorResponse =
			ErrorResponse.of(ex.javaClass.simpleName, ex.message!!)
		return super.handleExceptionInternal(ex, errorResponse, headers, statusCode, request)
	}

	/**
	 * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다. HttpMessageConverter 에서 등록한
	 * HttpMessageConverter binding 못할경우 발생 주로 @RequestBody, @RequestPart 어노테이션에서 발생
	 */
	override fun handleMethodArgumentNotValid(
		e: MethodArgumentNotValidException,
		headers: HttpHeaders,
		status: HttpStatusCode,
		request: WebRequest,
	): ResponseEntity<Any>? {
		log.error("MethodArgumentNotValidException : {}", e.message, e)
		val errorMessage: String? = e.bindingResult.allErrors[0].defaultMessage
		val errorResponse =
			ErrorResponse.of(e.javaClass.getSimpleName(), errorMessage!!)
		val response: GlobalResponse = GlobalResponse.fail(status.value(), errorResponse)
		return ResponseEntity.status(status).body<Any>(response)
	}

	/** Request Param Validation 예외 처리  */
	@ExceptionHandler(ConstraintViolationException::class)
	fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<GlobalResponse> {
		log.error("ConstraintViolationException : {}", e.message, e)

		val bindingErrors: MutableMap<String?, Any> = HashMap()
		e.constraintViolations
			.forEach(
				Consumer { constraintViolation: ConstraintViolation<*> ->
					val propertyPath =
						listOf(
							*constraintViolation
								.propertyPath
								.toString()
								.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray(),
						)
					val path =
						propertyPath.stream()
							.skip(propertyPath.size - 1L)
							.findFirst()
							.orElse(null)
					bindingErrors[path] = constraintViolation.message
				},
			)

		val errorResponse =
			ErrorResponse.of(e.javaClass.simpleName, bindingErrors.toString())
		val response: GlobalResponse =
			GlobalResponse.fail(HttpStatus.BAD_REQUEST.value(), errorResponse)
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
	}

	/** PathVariable, RequestParam, RequestHeader, RequestBody 에서 타입이 일치하지 않을 경우 발생  */
	@ExceptionHandler(MethodArgumentTypeMismatchException::class)
	protected fun handleMethodArgumentTypeMismatchException(
		e: MethodArgumentTypeMismatchException,
	): ResponseEntity<GlobalResponse> {
		log.error("MethodArgumentTypeMismatchException : {}", e.message, e)
		val errorCode: ErrorCode = ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH
		val errorResponse =
			ErrorResponse.of(e.javaClass.getSimpleName(), errorCode.message)
		val response: GlobalResponse =
			GlobalResponse.fail(errorCode.status.value(), errorResponse)
		return ResponseEntity.status(errorCode.status).body(response)
	}

	/** 지원하지 않은 HTTP method 호출 할 경우 발생  */
	override fun handleHttpRequestMethodNotSupported(
		e: HttpRequestMethodNotSupportedException,
		headers: HttpHeaders,
		status: HttpStatusCode,
		request: WebRequest,
	): ResponseEntity<Any>? {
		log.error("HttpRequestMethodNotSupportedException : {}", e.message, e)
		val errorCode: ErrorCode = ErrorCode.METHOD_NOT_ALLOWED
		val errorResponse =
			ErrorResponse.of(e.javaClass.getSimpleName(), errorCode.message)
		val response: GlobalResponse =
			GlobalResponse.fail(errorCode.status.value(), errorResponse)
		return ResponseEntity.status(errorCode.status).body(response)
	}

	/** CustomException 예외 처리  */
	@ExceptionHandler(CustomException::class)
	fun handleCustomException(e: CustomException): ResponseEntity<GlobalResponse> {
		log.error("CustomException : {}", e.message, e)
		val errorCode: ErrorCode = e.errorCode
		val errorResponse =
			ErrorResponse.of(errorCode.name, errorCode.message)
		val response: GlobalResponse =
			GlobalResponse.fail(errorCode.status.value(), errorResponse)
		return ResponseEntity.status(errorCode.status).body(response)
	}

	/** 500번대 에러 처리  */
	@ExceptionHandler(Exception::class)
	protected fun handleException(e: Exception): ResponseEntity<GlobalResponse> {
		log.error("Internal Server Error : {}", e.message, e)
		val internalServerError: ErrorCode = ErrorCode.INTERNAL_SERVER_ERROR
		val errorResponse =
			ErrorResponse.of(e.javaClass.simpleName, internalServerError.message)
		val response: GlobalResponse =
			GlobalResponse.fail(internalServerError.status.value(), errorResponse)
		return ResponseEntity.status(internalServerError.status).body(response)
	}
}
