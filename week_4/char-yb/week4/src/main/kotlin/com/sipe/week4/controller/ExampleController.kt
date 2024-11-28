package com.sipe.week4.controller

import com.sipe.week4.service.ExampleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/** 요청 흐름
 * 	1.	요청:
 * 	•	/test-coroutine 엔드포인트로 요청을 보냅니다.
 * 	2.	서비스에서 코루틴 실행:
 * 	•	ExampleService에서 코루틴이 실행됩니다.
 * 	•	Spring의 ThreadPoolTaskExecutor를 사용해 비동기적으로 작업을 실행.
 * 	3.	Dispatcher 동작:
 * 	•	SpringCoroutineDispatcher를 통해 작업이 TaskExecutor의 스레드풀에서 처리됩니다.
 */

@RestController
class ExampleController(
    private val exampleService: ExampleService
) {
    @GetMapping("/test-coroutine")
    fun testCoroutine(): String {
        exampleService.executeCoroutines()
        return "Coroutine started!"
    }
}