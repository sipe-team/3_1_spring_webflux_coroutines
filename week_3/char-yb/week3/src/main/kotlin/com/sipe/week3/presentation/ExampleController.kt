package com.sipe.week3.presentation

import com.sipe.week3.application.AsyncService
import com.sipe.week3.application.ThreadService
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api")
class ExampleController(
    private val asyncService: AsyncService,
    private val threadService: ThreadService,
) {

    @GetMapping("/async")
    fun getAsyncResult(): String = runBlocking {
        // 코루틴 기반으로 비동기 작업을 동시에 실행하고 결과 반환
        asyncService.executeAsyncTasks()
    }

    @GetMapping("/thread")
    fun getThreadResult(): String {
        // 스레드 기반으로 비동기 작업을 동시에 실행하고 결과 반환
        return threadService.executeThreadTasks()
    }
}