package com.sipe.week5.global.config.reactor

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@Configuration
class ReactorSchedulerConfig {
	@Bean
	fun ioScheduler(): Scheduler {
		return Schedulers.boundedElastic()
	}
}
