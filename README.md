# 3_1_reactive

어렵게 배워서, 쉽게 사용하는 Spring Webflux + Coroutines

### 팀 이름

극락코딩

### 주제

어렵게 배워서, 쉽게 사용하는 Spring Webflux + Coroutines

### 목표가 무엇인가요?

- Thread부터 RxJava를 거쳐~ Reactor로 달려가~~코루틴으로 종지부를 찍어요.
- Tomcat에서 시작해서 Netty(이희승님을 기리며) 까지 공부해요. (Spring mvc -> Spring Webflux)
- 위에 소개된 내용들에 대해, 깊이 있는 동작원리와 간단한 사용방식 및 차이를 학습해요.
- 코드 베이스적인 내용 + CS적인 내용을 함께~ (꼬리에 꼬리를 무어, 계속 계속 딥하게~)
- 결과적으로 Thread에 대한 깊이 있는 이해를 얻고자 합니다!!

### 어떻게 진행되나요?

- 1주차: Thread 기반 학습, M-threads, Spring MVC 등을 공부해요.
  - 모든 Task는 CPU에 스레드가 올라가며, 동작을 진행합니다. 그렇기 때문에 OS단과 JVM에서의 Thread 동작원리를 같이 공부해요.
- M-Threads를 대표하는 키워드 스프링 키워드에 대해 공부해요. CompletableFuture, Runnable, Callable, Executor, async 등..
  - Spring Tomcat의 스레드로부터, 비즈니스로직에서 사용되는 M-Threads와의 연관성을 같이 공부해요.
- 2주차: RxJava, Reactor를 공부해요.
  - Webflux 내부 코드를 까보면, Reactor이고, Reactor를 까보면, RxJava를 Base로 구성되어 있어요.
  - 그러면, 이런 라이브러리를 사용했을 때, OS Level, JVM Level에서 어떤 변화가 발생할까요?
- 3주차: Spring Webflux와 Reactor에서 사용되는 스레드 처리를 공부해요.
  - Webflux와 Reactor를 엮었을 때, peer to peer로 어떻게 동작하는지 동작원리를 확인해요.
  - Spring MVC + M-Threads -> Spring Webflux + Reactor로 넘어가면서 어떤 변화가 있을까요?
- 4주차: Corotuines에 대해 공부해요.
  - 요즘 유행하는 코루틴.. kotlin-coroutines에 대해 학습해요. (제일 중요한건 동작원리!)
  - 경량 스레드가 무엇일까요? OS와 JVM Level에서 공부해요.
- 5주차: Spring Webflux + Corotuines에서 사용되는 스레드 처리를 공부해요.
  - Webflux와 Corotuines를 엮었을 때, peer to peer로 어떻게 동작하는지 동작원리를 확인해요.
- 6주차: 비동기 서비스를 이용할 때 장점 그리고, 발생할 수 있는 이슈 등에 대해 경험을 공유해요!

### 학습 방법

- 각 주차에 정해진 주제와 목표를 달성해요!
- 서로 준비한 내용을 발표해요!
- 10분에서 15분 동안 강의를 진행한다고 생각하며 진행.
- 질문과 대답을 반복하며, 동료들과 지식을 공유
- 만약... 준비하지 못하면 벌금..
- 온라인으로 진행해요. 만약 지원금이 있다면..오프라인도~

### 주요키워드

Kotlin, Thread, Reactor, RxJava, Webflux, Tomcat, Netty, Coroutines, Mvc, M-threads

### 그래서 우리는... 이걸 중점으로 공부할거에요.

- 왜? Non-Blocking IO를 사용할까?
- 그렇다면, 이런 기술들이 OS Level과 JVM Level에서 어떤식으로 변할까?
- (궁극적으로 우리가 쓰는 기술이 하드웨어 장비와 연결되어, 어떤 방식으로 동작하는지 이해하는게, 제일 중요할 것 같습니다.)
