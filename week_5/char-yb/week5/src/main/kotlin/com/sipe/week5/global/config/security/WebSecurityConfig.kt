package com.sipe.week5.global.config.security
import com.sipe.week5.global.filter.JwtAuthenticationFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
	private val jwtTokenProvider: JwtTokenProvider,
	private val handlerExceptionResolver: HandlerExceptionResolver,
) {
	@Bean
	fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

	@Bean
	@Order(0)
	fun loginFilterChain(http: HttpSecurity): SecurityFilterChain =
		http
			.applyCommonConfigurations()
			.securityMatcher("/**", "/swagger-ui/**", "/v3/api-docs/**")
			.authorizeHttpRequests { it.anyRequest().permitAll() }
			.build()

	@Bean
	@Order(1)
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.applyCommonConfigurations()
			.authorizeHttpRequests { authorize ->
				authorize
					.requestMatchers("/auth/signIn").permitAll()
					.requestMatchers("/auth/signUp").permitAll()
					.anyRequest().authenticated()
			}
			.exceptionHandling { exception: ExceptionHandlingConfigurer<HttpSecurity?> ->
				exception.authenticationEntryPoint {
						_: HttpServletRequest?,
						response: HttpServletResponse,
						_: AuthenticationException?,
					->
					response.status = 401
				}
			}

		http.addFilterBefore(
			JwtAuthenticationFilter(jwtTokenProvider, handlerExceptionResolver),
			UsernamePasswordAuthenticationFilter::class.java,
		)

		return http.build()
	}

	@Bean
	fun corsConfigurationSource(): CorsConfigurationSource {
		val configuration =
			CorsConfiguration().apply {
				addAllowedOriginPattern("*") // TODO: CORS 임시 전체 허용
				addAllowedHeader("*")
				addAllowedMethod("*")
				allowCredentials = true
				addExposedHeader(HttpHeaders.SET_COOKIE)
			}
		return UrlBasedCorsConfigurationSource().apply {
			registerCorsConfiguration("/**", configuration)
		}
	}

	private fun HttpSecurity.applyCommonConfigurations(): HttpSecurity =
		this
			.httpBasic { it.disable() }
			.formLogin { it.disable() }
			.csrf { it.disable() }
			.cors(withDefaults())
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
}
