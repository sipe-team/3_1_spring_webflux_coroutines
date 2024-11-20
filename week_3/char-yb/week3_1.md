## 코루틴 개념, 코루틴을 왜 쓰는가?
- [코루틴 개념](https://en.wikipedia.org/wiki/Coroutine)
- [코루틴 docs](https://kotlinlang.org/docs/coroutines-overview.html#documentation)

### 코루틴 개념
코루틴, 영어로 하면 Co-Routine입니다.
Kotlin이니까 Ko-인줄 아실테지만, 그러니 Kotlin이 아닌 다른 언어에서도 코루틴이 제공되고 있습니다. Co는 협력, Routine은 간단히 함수라고 표현할 수 있습니다.
협력하는 함수라는 뜻으로, 프로그래밍에서 함수도 서로의 호출과 반환을 주고 받으며 협력을 합니다. Coroutine은 일종의 가벼운 스레드(Light-weight thread)로 동시성 작업을 간편하게 처리할 수 있게 해주는 역할을 수행하고 있습니다.

제공해주신 위키피디아 내용의 일부를 발췌해보았습니다.

> Coroutines are computer program components that allow execution to be suspended and resumed, generalizing subroutines for cooperative multitasking.

번역이 어렵죠? `코루틴은 실행을 일시 중지하고 재개할 수 있는 컴퓨터 프로그램 구성 요소로, 협력적 멀티태스킹(비선점적 멀티태스킹)을 위해 서브루틴을 일반화합니다.` 라는 의미로 Ok, 일시 중지하고 재개하고 그러한 내부의 협력을 위한 루틴을 활용한 프로그래밍 기법인 듯합니다?
하나씩 더 알아보겠습니다.

#### 비선점적 프로그래밍 (Non-Preemptive Programming)
컴퓨터구조에서 많이 들었던 개념인 것 같은데 비선점형은 하나의 태스크가 다른 태스크가 실행 중이어도 프로세서(CPU)를 차지할 수 있다. 반대로 선점형은 하나의 태스크가 다른 태스크가 실행 중이라면 프로세서(CPU)를 차지할 수 없다.
코루틴은 비선점형 멀티태스킹, 스레드는 선점형 멀티태스킹이다. 그러므로 코루틴은 병행성(=동시성)은 제공하고 병렬성은 제공하지 않는다.

병렬성(Parallel) vs 병행성(Concurrency)
병렬성은 물리적, 병행성은 논리적 개념에서 다루며, 병행성은 동시에 실행되는 것처럼 보이는 것.
병렬성은 실제로 동시에 작업이 처리가 되는 것.

#### 루틴 (Routine)
Routine은 하나의 Task 혹은 Function이라고 이해해도 될 거 같습니다. 보통 프로그램은 다양한 Routine들을 조합시켜 개발하는데. Routine은 Main Routine과 Sub Routine으로 나뉩니다. Main Routine이 Sub Routine을 호출하는 방식이며 Coroutine 또한 Routine의 한 종류이지만 다음과 같은 특징이 있습니다.

Main-Sub 개념을 구분하지 않습니다. 그러므로 모든 Routine들이 서로를 호출할 수 있고, 그러니 병행성을 가지고 있습니다. 진입과 탈출이 자유로우며 Sub Routine은 return을 만나야만 탈출할 수 있습니다. (고민점: return이 없으면 정말 Routine 탈출이 안될까?)

---
### 코루틴을 왜 사용하는가?

일반적인 CRUD 요구사항을 기반으로 성공 케이스를 따라가 개발하는 것은 사실 쉬운 개발입니다. 하지만 이제 우리가 회사라는 집단 속에서 "느리다", "빨리 좀..." 이런 VOC와 QA 사항을 전달받으면 개발을 하면서 가장 머리를 싸매야 하는 순간들입니다. 성능 개선을 시도하는 방법 중 한 가지 케이스로 어떤 코드를 동시에 처리를 해야 하는지, 반대로 그러면 안되는지를 결정하는 순간일 것입니다. 여기서 결정한다고 하더라도 코드를 작성했을 때 동시에 처리를 하는 경우 순서에 따라 결과가 달라져 문제를 해결할 수 없는 경우가 다반사일수도 있습니다. 특히 Android에서는 UI를 그리는 작업, 데이터를 받아오는 작업을 동시에 수행하는 경우가 많기 때문에 이를 제어하기 위해서는 비동기 처리는 필수적이며 코루틴을 사용하는 예시가 될 수 있습니다.

서버 개발자 관점에서도 당연히 그리는 작업도 작업이지만, DTO로 가공하거나 어떠한 데이터는 배제해야 하는 filter를 사용하고 distinct를 통해 중복도 제거를 할 수 있겠죠? 혹은 데이터베이스에서 필요한 데이터를 조회하기 위한 DB Connection을 맺어야 하는 순간이 다반사라고 생각합니다.

이를 빠르게 응답하기 위한 기술 중 하나가 코루틴이라고 볼 수 있습니다.
지금부터 왜 사용을 해야하는 지 장점을 나열해보겠습니다.


#### 장점

1. Routine간 협력을 통한 비선점적 멀티태스킹
Coroutine을 사용하면 비동기로 Routine을 실행하고 일반적인 Sub Routine과 다르게 진입과 탈출이 자유로워 Routine간 협력을 통해 비선점적 멀티태스킹을 가능하게 합니다.

2. 동시성 프로그래밍 지원
동시성 프로그래밍이란 2개 이상의 프로세스가 동시에 작업을 하는 상태를 말하는데, Coroutine은 단일 코어에서 실행되어야 하기 때문에 각 Routine을 교차 배치합니다. 다중 Thread를 이용하게 되면 각 Thread 간 교체 시 Context Switching 비용이 발생합니다.(우리 이거 공부 많이했죠?) 하지만 Coroutine은 하나의 Thread 내에서 스케줄링이 가능하기 때문에 경량 쓰레드(Light-weight thread)라고도 불립니다.

3. 쉬운 비동기 처리
Multi Thread와 비교했을 때 Thread 간 통신과 콜백 구조로 코드가 흐르지 않기 때문에 코드 흐름 파악이 쉽습니다.(가독성) 또한, 개발자가 직접 작업을 스케줄링하기 때문에 코드 작성이 간단하고 예상하지 못한 상황을 줄일 수 있습니다.


```kotlin
fun main() = runBlocking { // this: CoroutineScope
    launch { // launch a new coroutine and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }
    println("Hello") // main coroutine continues while a previous one is delayed
}
```

위 코드는 코틀린(kotlinlang) 공식 문서 페이지에 참조되어 있는 간단한 예제입니다.

launch는 코루틴 빌더 입니다. 나머지 코드와 동시에 새로운 코루틴을 시작하는데, 나머지 코드는 독립적으로 계속 작동합니다. 그래서 Hello가 먼저 출력되었습니다. 왜냐하면?

delay는 특별한 일시 중단 함수 입니다. 특정 시간 동안 코루틴을 일시 중단합니다 . 코루틴을 일시 중단해도 기본 스레드는 차단 되지 않지만 다른 코루틴이 실행되고 코드에 기본 스레드를 사용할 수 있습니다.

runBlocking 함수는 일반 루틴 세계와 코루틴 세계를 연결하는 함수입니다.
runBlocking의 이름은 이를 실행하는 스레드(이 경우 메인 스레드)가 runBlocking { ... } 내부의 모든 코루틴이 실행을 완료할 때까지 호출 기간 동안 차단된다는 것을 의미합니다. 스레드(일반 루틴 세계)는 비용이 많이 드는 리소스이고 이를 차단하는 것은 비효율적이며 종종 바람직하지 않기 때문입니다.

결국 정리하면 CoroutineScope 안에서 1초 뒤 World!를 출력하는 코루틴이 만들어졌고 Hello를 출력하는 코드는 해당 코루틴과 별도로 Main Coroutine에 존재하므로 Hello 다음에 World!가 출력되게 됩니다.


### 참고 링크
- https://mochaive.medium.com/coroutine-5119fda3bc65
- https://kotlinlang.org/docs/coroutines-basics.html#your-first-coroutine