package com.sipe.week5.global.filter
import com.sipe.week5.domain.auth.exception.AuthenticationInvalidTokenException
import com.sipe.week5.domain.auth.exception.AuthenticationTokenNotExistException
import com.sipe.week5.domain.member.domain.MemberRole
import com.sipe.week5.global.config.security.JwtTokenProvider
import com.sipe.week5.global.config.security.PrincipalDetails
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtAuthenticationFilter(
	private val jwtTokenProvider: JwtTokenProvider,
	private val handlerExceptionResolver: HandlerExceptionResolver,
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		try {
			val authenticationHeader = request.getHeader("Authorization") ?: throw AuthenticationTokenNotExistException()
			val accessToken =
				if (authenticationHeader.startsWith("Bearer ")) {
					authenticationHeader.substring(7)
				} else {
					throw AuthenticationInvalidTokenException()
				}

			val accessTokenDto = jwtTokenProvider.parseAccessToken(accessToken)
			println("memberId, memberRole: ${accessTokenDto.memberId}, ${accessTokenDto.memberRole}")
			setAuthenticationToContext(accessTokenDto.memberId, accessTokenDto.memberRole)
			return filterChain.doFilter(request, response)
		} catch (e: Exception) {
			handlerExceptionResolver.resolveException(request, response, null, e)
		}
	}

	private fun setAuthenticationToContext(
		memberId: Long,
		memberRole: MemberRole,
	) {
		val userDetails: UserDetails = PrincipalDetails(memberId, memberRole)

		val authentication: Authentication =
			UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.authorities,
			)
		SecurityContextHolder.getContext().authentication = authentication
	}
}


