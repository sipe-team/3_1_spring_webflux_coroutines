package com.sipe.week5.global.config.properties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(
	JwtProperties::class,
)
@Configuration
class PropertiesConfig
