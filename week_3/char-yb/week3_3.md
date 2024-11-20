## 간단 실습

- api 하나 만드는데, I/O 작업 3개 이상이 있다.
- 3개를 동시에 실행시키고, 동시에 완료된 이후에 return하도록 코루틴을 기반으로 구성.
- 하나는 스레드 기반으로 해봐서 비교해보기

#### 개발 환경

**의존성**

```groovy
dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```

#### Controller 코드

```kotlin
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
```

간단한 메모리 상에서 동작되는 API 두 개를 예제로 들어보겠습니다.
Controller는 흔하게 볼 수 있는 RestController 어노테이션으로 만든 코드입니다.
해당 API는 두 가지로, `/api/thread`, `/api/async`를 개발하였습니다.

#### Service 코드

getThreadResult는 스레드 기반으로 비동기 작업을 동시에 실행하고 결과 반환하며,
getAsyncResult는 코루틴을 기반으로 마찬가지 비동기 작업을 동시에 실행하여 결과를 반환합니다.

간단하게 Service 코드를 설명하자면,

1. ThreadService

```kotlin
package com.sipe.week3.application

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

@Service
class ThreadService {

    private val executor = Executors.newFixedThreadPool(3) // 3개의 스레드를 사용

    fun executeThreadTasks(): String {
        // 각 작업의 시작 시간 기록
        println("Thread tasks started at: ${LocalDateTime.now()}")

        // 3개의 I/O 작업을 동시에 실행
        val task1: Future<String> = executor.submit(Callable { ioOperation("Task 1") })
        val task2: Future<String> = executor.submit(Callable { ioOperation("Task 2") })
        val task3: Future<String> = executor.submit(Callable { ioOperation("Task 3") })

        // 모든 작업이 완료된 후 결과를 반환
        val result = "${task1.get()}, ${task2.get()}, ${task3.get()}"

        // 모든 작업이 완료된 시간 기록
        println("Thread tasks completed at: ${LocalDateTime.now()}")

        return result
    }

    // I/O 작업 시뮬레이션 함수 (1초 지연)
    private fun ioOperation(taskName: String): String {
        println("$taskName started at: ${LocalDateTime.now()}")
        Thread.sleep(100) // 실제 I/O 작업을 스레드 기반으로 처리하는 부분 (예: DB 조회, 외부 API 호출)
        println("$taskName completed at: ${LocalDateTime.now()}")
        return "$taskName completed"
    }
}
```

- ExecutorService를 이용하여 3개의 스레드 풀을 생성하고, submit 메서드를 사용하여 각각의 ioOperation 작업을 실행합니다.
- 각 작업은 Future 객체로 반환되며, get 메서드를 호출하여 모든 작업이 완료될 때까지 기다린 후 결과를 얻습니다.
- 만약 비즈니스 로직에 추가가 된다면 스레드가 차지하는 리소스가 커서, 요청이 많아질 시 성능에 영향을 줄 수도 있겠습니다.

2. AsyncService

```kotlin
package com.sipe.week3.application

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AsyncService {

    fun executeAsyncTasks(): String = runBlocking {
        // 각 작업의 시작 시간 기록
        println("Coroutine tasks started at: ${LocalDateTime.now()}")

        // 3개의 I/O 작업을 동시에 실행
        val task1 = async { ioOperation("Task 1") }
        val task2 = async { ioOperation("Task 2") }
        val task3 = async { ioOperation("Task 3") }

        // 모든 작업이 완료된 후 결과를 반환
        val result = "${task1.await()}, ${task2.await()}, ${task3.await()}"

        // 모든 작업이 완료된 시간 기록
        println("Coroutine tasks completed at: ${LocalDateTime.now()}")

        result
    }

    // I/O 작업 시뮬레이션 함수 (비동기적으로 1초 지연)
    private suspend fun ioOperation(taskName: String): String {
        println("$taskName started at: ${LocalDateTime.now()}")
        delay(100) // 실제 I/O 작업을 비동기적으로 처리하는 부분 (예: DB 조회, 외부 API 호출)
        println("$taskName completed at: ${LocalDateTime.now()}")
        return "$taskName completed"
    }
}
```

(벌써 기대가 되는 코드...)

- async 코루틴 빌더를 사용하여 동시에 I/O 작업을 실행합니다.
- 각 async 블록에서 ioOperation 함수를 호출하며, 이 함수는 delay로 0.1초 지연 후 작업 완료 메시지를 반환합니다.
- await를 사용하여 모든 작업이 완료될 때까지 기다린 후 결과를 반환합니다.
- 이 방식은 코루틴의 비동기 특성을 활용하여 효율적으로 I/O 작업을 동시에 수행하고 있습니다.

이 코드들을 직접 실행하여 Postman으로 한 번씩 호출해보았습니다.

**우선 스레드 기반 코드의 Service 로직 실행 시 출력값은?**

```
Thread tasks started at: 2024-11-19T22:52:39.136537
Task 3 started at: 2024-11-19T22:52:39.138334
Task 2 started at: 2024-11-19T22:52:39.138354
Task 1 started at: 2024-11-19T22:52:39.138336
Task 3 completed at: 2024-11-19T22:52:39.243569
Task 1 completed at: 2024-11-19T22:52:39.243569
Task 2 completed at: 2024-11-19T22:52:39.243607
Thread tasks completed at: 2024-11-19T22:52:39.246085
```

흠... 생각보다 빠르고 비동기로 진행되어서 그런가~ 3,1,2의 태스크가 동작되는 것을 볼 수 있죠??

**다음으로는 코루틴 기반 코드의 Service 로직 실행 결과입니다.**

```
Coroutine tasks started at: 2024-11-19T22:54:13.871685
Task 1 started at: 2024-11-19T22:54:13.872250
Task 2 started at: 2024-11-19T22:54:13.872302
Task 3 started at: 2024-11-19T22:54:13.872314
Task 1 completed at: 2024-11-19T22:54:13.977500
Task 2 completed at: 2024-11-19T22:54:13.977739
Task 3 completed at: 2024-11-19T22:54:13.977776
Coroutine tasks completed at: 2024-11-19T22:54:13.977851
```

흠... 얘도 뭔가 빠른데 호출을 한 번씩 계속해보면 1,2,3 이렇게 태스크가 동작되는 것은 확인해볼 수 있었어요.

메모리 상 성능 테스트를 진행했을 땐 뭔가 "와 대단하다"라는 것을 체감할 수는 없었습니다.

그래서 GPT에게 좀 물어봤습니다.

```
출력문을 보면 코루틴보다 스레드가 더 성능이 빠른 거처럼 보여.
뭔가 트래픽을 일부러 주어서 코루틴에 트래픽을 받을때 더 성능이 좋다는 걸 증명하고 싶은데 어떤 방법으로 증명할 수 있을까??
```

네 제가 억지로 코루틴을 치켜 세워 만들어낸 질문입니다.

아무튼 테스트의 목표부터 결과 비교까지 자세히 알려주었는데요,

**결과 비교 관점**

- 평균 응답 시간과 성공률을 비교합니다.
- 스레드 기반 서비스는 스레드 풀의 한계로 인해 점차 응답 시간이 길어지거나, 스레드 부족으로 인해 요청이 대기 상태에 빠질 수 있습니다.
- 반면, 코루틴 기반 서비스는 경량화된 구조 덕분에 더 많은 동시 요청을 효율적으로 처리할 수 있으며, 상대적으로 응답 시간이 덜 증가하게 됩니다.

**테스트 도구**
JMeter랑 Gatling은 저도 들어는 봤고 성능 테스트 도구로 많이들 사용하는 걸로 알고 있습니다.
그런데 Apache Bench (ab)는 이번에 처음 알았는데, 기본적으로 apache에서 제공되는 것으로 보이며 터미널에 ab라고 명령어를 실행하시면 됩니다.

```
# 스레드 기반 API에 1000개의 동시 요청 보내기
ab -n 1000 -c 100 "http://localhost:8080/api/thread"

# 코루틴 기반 API에 1000개의 동시 요청 보내기
ab -n 1000 -c 100 "http://localhost:8080/api/async"
```

터미널에서 Apache Bench를 사용하여 동시에 1000개의 요청을 보내는 예시입니다.

- -n 1000: 총 1000개의 요청을 보냄
- -c 100: 동시에 100개의 요청을 병렬로 보냄

Apache Bench는 평균 응답 시간과 성공률을 보여주며, 이 결과를 통해 코루틴 기반 방식이 더 많은 트래픽에서 어떻게 성능을 발휘하는지 확인할 수 있습니다.

```
## GPT의 예상 결과

- 코루틴 기반 서비스는 10개의 작업을 동시에 실행하면서도 스레드 리소스를 절약하며 효율적으로 동시성을 유지합니다.
- 반면, 스레드 기반 서비스는 물리적 스레드 풀의 한계로 인해 스레드 오버헤드가 발생하고, 작업이 지연될 가능성이 높습니다.
- 요청이 많아질수록, 스레드 풀의 크기가 제한되어 있기 때문에 스레드 기반 서비스는 일정 수준 이상에서 성능이 크게 저하될 수 있습니다.

결과 분석 및 증명

이와 같은 부하 테스트를 통해 코루틴 기반 서비스가 더 적은 자원으로 높은 트래픽을 처리할 수 있다는 점을 확인할 수 있습니다. 특히, 많은 요청이 동시에 발생하는 상황에서 스레드 기반 서비스는 응답 시간이 길어지고 성공률이 낮아지지만, 코루틴 기반 서비스는 상대적으로 더 안정적인 성능을 보일 것입니다.

이 실험을 통해 코루틴의 동시성 처리와 효율성을 증명할 수 있으며, 이를 통해 고성능 비동기 작업 환경에서 코루틴이 스레드 기반 방식보다 우수함을 확인할 수 있습니다.
```

확인해볼까요?
**우선 스레드 기반 실행 결과입니다.**

```
## 터미널 실행 명령어: ab -n 1000 -c 100 "http://localhost:8080/api/thread"

This is ApacheBench, Version 2.3 <$Revision: 1903618 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 100 requests
Completed 200 requests
Completed 300 requests
Completed 400 requests
Completed 500 requests
Completed 600 requests
Completed 700 requests
Completed 800 requests
Completed 900 requests
Completed 1000 requests
Finished 1000 requests


Server Software:
Server Hostname:        localhost
Server Port:            8080

Document Path:          /api/thread
Document Length:        52 bytes

Concurrency Level:      100
Time taken for tests:   104.055 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      185000 bytes
HTML transferred:       52000 bytes
Requests per second:    9.61 [#/sec] (mean)
Time per request:       10405.518 [ms] (mean)
Time per request:       104.055 [ms] (mean, across all concurrent requests)
Transfer rate:          1.74 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.4      0       7
Processing:   111 9866 1834.1  10391   10438
Waiting:      111 9865 1834.1  10390   10438
Total:        114 9867 1833.1  10391   10439

Percentage of the requests served within a certain time (ms)
  50%  10391
  66%  10398
  75%  10402
  80%  10406
  90%  10413
  95%  10418
  98%  10422
  99%  10425
 100%  10439 (longest request)
```

Benchmarking localhost (be patient)을 보시면 0.1초의 딜레이를 주었고 100개의 요청을 끝낼 시에는 총 10초의 시간이 걸립니다.

Time taken for tests: 104.055 seconds
그래서 위처럼 테스트 실행 시간은 104초 가량 결과를 확인할 수 있습니다.

**자 이제 코루틴 기반의 실행결과입니다.**

```
## 터미널 실행 명령어: ab -n 1000 -c 100 "http://localhost:8080/api/async"

This is ApacheBench, Version 2.3 <$Revision: 1903618 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 100 requests
Completed 200 requests
Completed 300 requests
Completed 400 requests
Completed 500 requests
Completed 600 requests
Completed 700 requests
Completed 800 requests
Completed 900 requests
Completed 1000 requests
Finished 1000 requests


Server Software:
Server Hostname:        localhost
Server Port:            8080

Document Path:          /api/async
Document Length:        52 bytes

Concurrency Level:      100
Time taken for tests:   1.245 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      185000 bytes
HTML transferred:       52000 bytes
Requests per second:    803.28 [#/sec] (mean)
Time per request:       124.490 [ms] (mean)
Time per request:       1.245 [ms] (mean, across all concurrent requests)
Transfer rate:          145.12 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.6      1      10
Processing:   100  105   3.5    105     162
Waiting:      100  105   3.4    105     159
Total:        101  107   4.2    106     162

Percentage of the requests served within a certain time (ms)
  50%    106
  66%    107
  75%    108
  80%    109
  90%    113
  95%    115
  98%    117
  99%    117
 100%    162 (longest request)
```

실행을 해보셨다면 정말 체감이 잘 되는데, Time taken for tests: 1.245 seconds의 수치적으로도 확연이 보이고, Processing 결과보시면 압도적인 차이를 보실 수 있습니다.

## 결과 분석

위의 결과에서 코루틴 기반 방식과 스레드 기반 방식의 성능 차이를 확인할 수 있습니다. 아래에 주요 항목을 비교하여 분석했습니다.

1. 총 실행 시간 (Time taken for tests)

- 코루틴 기반: 1.245 seconds
- 스레드 기반: 104.055 seconds

코루틴 방식은 1.245초에 1000개의 요청을 처리한 반면, 스레드 기반 방식은 104.055초가 걸렸습니다. 이는 코루틴 방식이 스레드 방식보다 80배 이상 빠르다는 것을 보여줍니다. 코루틴은 비동기적으로 작업을 처리하여, 훨씬 빠르게 요청을 완료할 수 있습니다.

2. 초당 요청 처리량 (Requests per second)

- 코루틴 기반: 803.28 [#/sec]
- 스레드 기반: 9.61 [#/sec]

코루틴 방식은 초당 803.28개의 요청을 처리한 반면, 스레드 기반 방식은 초당 9.61개의 요청을 처리했습니다. 코루틴이 스레드 방식보다 훨씬 높은 요청 처리량을 보여줍니다. 이는 코루틴이 경량 스레드처럼 동작하며, 효율적으로 자원을 활용해 더 많은 요청을 동시에 처리할 수 있기 때문입니다.

3. 각 요청의 평균 응답 시간 (Time per request)

- 코루틴 기반 (mean): 124.490 ms
- 스레드 기반 (mean): 10405.518 ms

각 요청에 대해 코루틴 방식은 평균 124.49ms의 응답 시간을 보인 반면, 스레드 방식은 10405.518ms의 응답 시간을 보였습니다. 이는 코루틴 방식이 훨씬 빠르게 응답을 반환한다는 것을 의미합니다.

4. 최대 요청 시간 (Longest request)

- 코루틴 기반: 162 ms
- 스레드 기반: 10439 ms

코루틴 방식에서 가장 오래 걸린 요청의 처리 시간은 162ms로, 스레드 방식에서 가장 오래 걸린 10439ms와 비교하면 매우 짧습니다. 이는 코루틴 방식이 요청을 처리하는데 훨씬 안정적인 성능을 보장한다는 점을 보여줍니다.

5. 응답 시간의 분포

코루틴 기반:
- 50%의 요청이 106ms 이내에 처리되었습니다.
- 90%의 요청이 113ms 이내에 처리되었습니다.
- 모든 요청이 162ms 이내에 처리되었습니다.

스레드 기반:
- 50%의 요청이 10391ms 이내에 처리되었습니다.
- 90%의 요청이 10413ms 이내에 처리되었습니다.
- 모든 요청이 10439ms 이내에 처리되었습니다.

코루틴 방식은 요청의 처리 시간이 일정하게 분포되어 있으며, 대부분의 요청이 짧은 시간 내에 처리되었습니다. 반면, 스레드 방식은 평균 응답 시간이 훨씬 길고, 요청이 밀릴수록 대기 시간이 늘어나게 됩니다.

왜 코루틴 방식이 더 좋은가?

1. 경량화된 동시성 처리: 코루틴은 실제 OS 스레드를 점유하지 않으면서도 비동기 작업을 처리할 수 있어, 스레드보다 적은 리소스로 더 많은 동시 작업을 처리할 수 있습니다. 특히 많은 수의 I/O 작업이 있는 경우, 코루틴은 자원 사용 효율성이 높아집니다.

2. 스레드 오버헤드 감소: 스레드는 생성 및 전환 시 오버헤드가 발생합니다. 특히, 고밀도의 동시 요청이 들어올 때 스레드 풀의 크기가 한계에 도달하면 스레드 기반 방식은 병목 현상이 발생합니다. 반면, 코루틴은 스레드 전환 없이도 많은 작업을 효율적으로 처리할 수 있습니다.

3. 높은 트래픽에 강한 안정성: 스레드 방식은 트래픽이 많아질수록 OS 레벨의 스레드 풀 한계에 부딪혀 응답 속도가 느려지며, 일정 수준 이상에서 성능이 급격히 저하됩니다. 코루틴은 메모리 사용량이 적고, 비동기적으로 처리되므로 트래픽이 높아져도 비교적 안정적인 성능을 유지할 수 있습니다.

4. 비동기 I/O 작업에 최적화: 코루틴은 비동기 I/O 작업을 효율적으로 처리할 수 있도록 설계되었습니다. 코루틴은 suspend 함수와 같은 비동기 작업을 통해 스레드를 차단하지 않고 다른 작업으로 전환할 수 있습니다. 반면, 스레드 기반 방식에서는 스레드가 I/O 작업을 기다리는 동안 다른 작업을 처리하지 못합니다.

### 결론

- 코루틴 기반 방식은 요청이 많거나 동시성이 중요한 상황에서 훨씬 효율적이며 안정적인 성능을 제공합니다. 코루틴은 적은 자원으로 높은 트래픽을 처리할 수 있어, 특히 네트워크 I/O와 같은 비동기 작업에 적합합니다.
- 스레드 기반 방식은 동시 요청이 증가하면 성능이 급격히 저하되며, 스레드 풀의 한계로 인해 시스템 자원을 과도하게 사용할 수 있습니다. 따라서 높은 동시성 요구사항을 충족하기에는 한계가 있습니다.

이러한 결과를 통해 Spring Boot 환경에서 코루틴을 사용하면 높은 트래픽 환경에서도 안정적인 성능을 유지할 수 있다는 것을 확인할 수 있습니다. 특히 비동기 작업이 많은 경우 코루틴의 장점이 돋보였습니다.

코루틴 그는 신인가.. **G O A T** 