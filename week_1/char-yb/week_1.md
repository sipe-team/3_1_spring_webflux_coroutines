## 1주차
- Thread, Runnable, Callable, ExecuterService, Async, CompletableFure, ThreadLocal
- Atomic (CAS), Syncronized (lock), voilate, FolkJoinPool, BlockingDeque
- JVM에서 스레드 동작하는 방식
- 컨텍스트 스위칭 비용이란?
- 병렬 프로그램시 알아야할 인프라 리소스

--- 
## Thread 
스레드는 프로세스 내에서 실행되는 작은 단위
독립적으로 실행될 수 있는 코드의 실행 흐름이라 볼 수도 있다.
JVM 환경에서는 멀티 스레드 환경으로 하나의 프로세스에서 여러 개의 스레드가 존재할 수 있으며, 각 스레드는 동시에 실행된다.

Java에서 java.lang.Thread 클래스를 사용하여 스레드를 생성하게 되는데, 생성된 스레드는 일반적으로 Runtime 환경에서 Runnable 객체로 전달된다.

각 스레드는 우선순위를 가지고, 높은 우선순위를 갖는 스레드는 낮은 우선순위를 갖는 스레드보다 CPU 자원을 더 많이 할당받을 수 있다고 한다.

이렇게 한 프로세스에서 다중 스레드를 다루는 환경에서 여러 스레드 인스턴스가 공유 자원에 동시에 접근할 수 있으니 동기화를 통해 데이터의 무결성을 유지하고 스레드에 대한 안정성을 보장하도록 설계해야 한다.

이렇게 스레드에 대한 동작을 핸들링하기 위해 제공되는 메서드가 존재한다.

1. sleep
- 현재 스레드를 멈추기
- 자원을 놓아주지는 않고, 제어권을 넘겨주므로 데드락이 발생할 수 있다.
    - 자원이 계속 서로간의 대해 눈치만 한없이 보는거임.

2. interrupt
- 다른 스레드를 "야! 일어나!" 깨워서 interruptedException을 발생시키도록 한다.
- interrupt가 발생한 스레드는 예외를 catch하여 다른 작업을 할 수 있다. 

3. join
- 다른 스레드의 작업이 끝날 때까지 기다리게 한다.
- 스레드의 순서를 제어할 때 사용할 수 있다.

## Runnable 



## Callable 



## ExecuterService 



## Async 



## CompletableFure 



## ThreadLocal 



