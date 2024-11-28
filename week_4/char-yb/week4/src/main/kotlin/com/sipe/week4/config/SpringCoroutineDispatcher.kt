package com.sipe.week4.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.springframework.core.task.TaskExecutor
import kotlin.coroutines.CoroutineContext

class SpringCoroutineDispatcher(
    private val taskExecutor: TaskExecutor
) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        taskExecutor.execute(block) // TaskExecutor를 사용하여 코루틴 실행
    }
}