package com.sipe.week5.global.common.response

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice(basePackages = ["com.sipe.week5"])
class GlobalResponseAdvice : ResponseBodyAdvice<Any> {
	override fun supports(
		returnType: MethodParameter,
		converterType: Class<out HttpMessageConverter<*>>,
	): Boolean {
		return true
	}

	override fun beforeBodyWrite(
		body: Any?,
		returnType: MethodParameter,
		selectedContentType: MediaType,
		selectedConverterType: Class<out HttpMessageConverter<*>>,
		request: ServerHttpRequest,
		response: ServerHttpResponse,
	): Any? {
		val servletResponse = (response as? ServletServerHttpResponse)?.servletResponse ?: return body
		val status = servletResponse.status
		val resolve = HttpStatus.resolve(status)

		return when {
			resolve == null || body == null || body is String -> body
			resolve.is2xxSuccessful -> GlobalResponse.success(status, body)
			else -> body
		}
	}
}
