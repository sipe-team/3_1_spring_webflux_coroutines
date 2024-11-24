# 3_1_spring_webflux_coroutines

어렵게 배워서, 쉽게 사용하는 Spring Webflux + Coroutines

---

### 팀 이름

극락코딩

---

### 발표순서

발표순서 정하기
https://lazygyu.github.io/roulette/

---

### 팀원

<table align="center">
    <th align="center">김동건</th>
    <th align="center">김재영</th>
    <th align="center">김지헌</th>
    <th align="center">정준서</th>
    <th align="center">차윤범</th>
    <tr>
        <td align="center">
            <a href="https://github.com/DongGeon0908"><img src="https://github.com/DongGeon0908.png" width="250"/></a>
        </td>
        <td align="center">
            <a href="https://github.com/jaeyeong951"><img src="https://github.com/jaeyeong951.png" width="250"/></a>
        </td>
        <td align="center">
            <a href="https://github.com/positivehun"><img src="https://github.com/positivehun.png" width="250"/></a>
        </td>
        <td align="center">
            <a href="https://github.com/sunseo18"><img src="https://github.com/sunseo18.png" width="250"/></a>
        </td>
        <td align="center">
            <a href="https://github.com/char-yb"><img src="https://github.com/char-yb.png" width="250"/></a>
        </td>
    </tr>

</table>

---

### 주제

어렵게 배워서, 쉽게 사용하는 Spring Webflux + Coroutines

---

### 목표가 무엇인가요?

- Thread부터 RxJava를 거쳐~ Reactor로 달려가~~코루틴으로 종지부를 찍어요.
- Tomcat에서 시작해서 Netty(이희승님을 기리며) 까지 공부해요. (Spring mvc -> Spring Webflux)
- 위에 소개된 내용들에 대해, 깊이 있는 동작원리와 간단한 사용방식 및 차이를 학습해요.
- 코드 베이스적인 내용 + CS적인 내용을 함께~ (꼬리에 꼬리를 무어, 계속 계속 딥하게~)
- 결과적으로 Thread에 대한 깊이 있는 이해를 얻고자 합니다!!

---

### 일정

- 수요일 오후 10시 30분 (1시간 진행)
- 벌금
  - 참여, 자료 제출은 별로 벌금... 각 2만원...
  - 전날까지 PR 올리기 (화요일)
  - 참여 까방권 1회 (그래도 자료 제출은 그날까지 제출하기.)
  - 수요일 10시까지는 자료 제출하기!

---

### 어떻게 진행되나요?

- 1주차 (동작방식에 대해 깊이 있게 설명, 시범기간...)

  - Thread, Runnable, Callable, ExecutorService, Async, CompletableFuture, ThreadLocal
  - Atomic (CAS), Syncronized (lock), voilate, FolkJoinPool, BlockingDeque
  - JVM에서 스레드 동작하는 방식
  - 컨텍스트 스위칭 비용이란?
  - 병렬 프로그램시 알아야할 인프라 리소스
  - 요거하고 피드백 진행!!

- 2주차
    - 컨텍스트 스위치 비용 (하드웨어적으로) - 10분
    - Atomic (CAS), Syncronized (lock), voilate, (FolkJoinPool, BlockingDeque, java.util.concurrent) - 10분
    - Tomcat 네트워크 요청을 받아서, 스레드를 할당받고, 이게 스프링까지 넘어와서 어떤식으로 스레드가 처리되는지? - 10분

- 3주차
    - 코루틴 개념, 코루틴을 왜 쓰는가? - 10분
        - [코루틴 개념](https://en.wikipedia.org/wiki/Coroutine)
        - [코루틴 docs](https://kotlinlang.org/docs/coroutines-overview.html#documentation)
    - 코루틴의 동작원리, 스레드와의 차이  - 10분
    - 간단 실습? - 10분
        - api 하나 만드는데, io작업 3개 이상이 있다.
        - 3개를 동시에 실행시키고, 동시에 완료된 이후에 return하도록 코루틴을 기반으로 구성.
        - 하나는 스레드 기반으로 해보기~     

- 4주차
  - 다음의 키워드에 대해 학습을 진행 (실습 포함) 
      - continuation, Dispatchers, async, launch, suspend, coroutineScope, coroutineContext, yield, runBlocking, withContext
    

- 1주차 ~ 2주차: Thread 기반 학습, M-threads, Spring MVC 등을 공부해요.
  - 모든 Task는 CPU에 스레드가 올라가며, 동작을 진행합니다. 그렇기 때문에 OS단과 JVM에서의 Thread 동작원리를 같이 공부해요.
  - M-Threads를 대표하는 키워드 스프링 키워드에 대해 공부해요. CompletableFuture, Runnable, Callable, Executor, async 등..
  - Spring Tomcat의 스레드로부터, 비즈니스로직에서 사용되는 M-Threads와의 연관성을 같이 공부해요.
- 3주차 ~ 4주차: Corotuines에 대해 공부해요.
  - 요즘 유행하는 코루틴.. kotlin-coroutines에 대해 학습해요. (제일 중요한건 동작원리!)
  - 경량 스레드가 무엇일까요? OS와 JVM Level에서 공부해요.
- 5주차: Spring Webflux + Corotuines에서 사용되는 스레드 처리를 공부해요.
  - Webflux와 Corotuines를 엮었을 때, peer to peer로 어떻게 동작하는지 동작원리를 확인해요.
- 6주차: 비동기 서비스를 이용할 때 장점 그리고, 발생할 수 있는 이슈 등에 대해 경험을 공유해요!

---

### 학습 방법

- 각 주차에 정해진 주제와 목표를 달성해요!
- 서로 준비한 내용을 발표해요!
- 10분에서 15분 동안 강의를 진행한다고 생각하며 진행.
- 질문과 대답을 반복하며, 동료들과 지식을 공유
- 만약... 준비하지 못하면 벌금..
- 온라인으로 진행해요. 만약 지원금이 있다면..오프라인도~

---

### 주요키워드

Kotlin, Thread, Reactor, RxJava, Webflux, Tomcat, Netty, Coroutines, Mvc, M-threads

---

### 그래서 우리는... 이걸 중점으로 공부할거에요.

- 왜? Non-Blocking IO를 사용할까?
- 그렇다면, 이런 기술들이 OS Level과 JVM Level에서 어떤식으로 변할까?
- (궁극적으로 우리가 쓰는 기술이 하드웨어 장비와 연결되어, 어떤 방식으로 동작하는지 이해하는게, 제일 중요할 것 같습니다.)
