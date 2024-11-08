# 2주차

- 컨텍스트 스위치 비용 (하드웨어적으로) - 10분
- Atomic (CAS), Syncronized (lock), voilate, (FolkJoinPool, BlockingDeque, java.util.concurrent) - 10분
- Tomcat 네트워크 요청을 받아서, 스레드를 할당받고, 이게 스프링까지 넘어와서 어떤식으로 스레드가 처리되는지? - 10분

<br>
<hr>
<br>
<br>
<br>


# 컨텍스트 스위치 비용 (하드웨어적으로)

### 스레드와 프로세스에 대해...

- 자원 적인 측면
  - 프로세스간에는 자원을 모두 독립적으로 사용, 반면, 스레드의 경우에는 자원을 공유하여 사용
  - 프로세스간 작업 변경 진행시, 분리된 자원으로 스위칭 비용이 큼, 반면에 스레드의 스위칭은 동일 메모리이기 때문에 상대적으로 빠름

- 멀티 프로세스와 멀티스레딩 차이
  - 스레드의 경우, 동일 프로세스내에서 병렬 처리 진행
  - 프로세스의 경우, 다중 프로세스로 병렬 처리 진행

<br>
<hr>
<br>

### 병렬 처리와 동시성 처리의 차이

- 자원적인 측면
  - 병렬처리의 경우, core에 각각 스레드가 할당되어 작업을 처리하는 것을 의미
    - 스레드 n개를 병렬처리할려면, core도 n개가 있어야 한다.
  - 동시성 처리의 경우, core 1개에서 빠르게 스레드가 컨텍스트 스위칭 되면서 작업을 진행
    - ![image](https://github.com/user-attachments/assets/7e7e2086-b565-44e4-bc0e-0640dc4db033)

<br>
<hr>
<br>

### 병렬 프로세스에 대해 리소스 관점에서 고민해야할 부분은?

- CPU의 Core수에 따라 병렬처리가 가능한지 아닌지 알 수 있다.
  - core가 1개이면, 동시성 처리만 가능 

<br>
<hr>
<br>

### 컨텍스트 스위칭이란?

- 컨텍스트 스위칭은 CPU의 Core에 올라간 스레드가 짧은 주기를 가지고 계속 바뀌는 것을 의미
  - 빠르게 스레드를 스위칭하면서, 작업의 정보를 PCB 등에 기록
  - ![image](https://github.com/user-attachments/assets/2ba008c5-bccd-4024-8ef9-eccd3dfb450b)
  - 스레드가 스위칭되면서, 스레드별 고유한 메모리 영역인 stack 정보가 계속 바뀌게 된다.
  - ![image](https://github.com/user-attachments/assets/92b513f4-0ce3-4457-a348-c99727ae539c)

<br>
<hr>
<br>

### 컨텍스트 스위칭에 대해 우리가 고려해야 할 부분

- 과도한 컨텍스트 스위칭이 리소스에 어떤 문제를 야기할까?
  - 다수의 스레드가 특정 작업을 진행하는데 있어, 코어를 점유하지 못하고 계속 스위칭된다면, 전반적인 스레드 작업이 밀릴 수 있다.

<br>
<hr>
<br>

### 그외 컨텍스트 스위칭 비용 관련 내용

- 1. 스레드 및 프로세스 수 관리
  - 스레드 및 프로세스 수 제한: 많은 스레드나 프로세스가 실행되면 스케줄러는 빈번하게 작업을 교체해야 하므로 컨텍스트 스위칭 비용이 증가 -> 최소한의 스레드로 필요한 작업을 수행할 수 있도록 설계 필요.
  - 적절한 스레드 풀 크기 설정: 스레드 풀이 지나치게 크면 오버헤드가 증가하고, 너무 작으면 작업 지연이 생길 수 있음. -> CPU 코어 수와 애플리케이션의 부하를 고려하여 적절한 크기로 설정하는 것이 중요
- 2. CPU 캐시 효율성
  - 캐시 무효화 주의: 프로세스 또는 스레드를 전환할 때 CPU 캐시가 무효화되면 캐시 히트율이 떨어짐 -> 특히 데이터 처리가 많은 애플리케이션에서는 캐시 무효화가 성능에 큰 영향을 줄 수 있으므로, 같은 데이터에 반복적으로 접근하는 패턴을 만들거나 데이터 접근을 최적화하는 방식이 유리.
- 3. 동기화와 락(lock) 관리
  - 락 경합(lock contention) 최소화: 다중 스레드 환경에서는 공유 자원을 보호하기 위해 락이 필요할 수 있지만, 락이 많아지면 스레드 간 경합이 발생하고 컨텍스트 스위칭이 증가 -> 락이 필요한 부분을 최소화하고, 가능한 경우 락 프리(lock-free) 자료구조나 비동기적 방식을 고려
  - 락 범위 좁히기: 락이 걸린 영역(critical section)을 최대한 좁게 설정하면, 동기화 문제로 인한 불필요한 컨텍스트 스위칭을 줄일 수 있음.
- 4. I/O 작업 및 비동기 처리
  - 블로킹 I/O 회피: 네트워크나 파일 입출력과 같은 블로킹 I/O 작업은 스레드가 대기 상태에 들어가게 하므로, 그 사이에 다른 스레드로 전환되어 컨텍스트 스위칭이 발생합 -> 비동기 I/O 방식(예: Java NIO나 Spring WebFlux)을 사용하면 CPU가 대기 없이 다른 작업을 처리할 수 있어 오버헤드를 줄일 수 있음.
  - 적절한 비동기 API 사용: 비동기 API나 코루틴을 사용할 때에도 과도한 작업 분할이 이뤄지면 오히려 스레드 전환이 잦아질 수 있으므로, 적절히 조정된 비동기 작업을 유지하는 것이 중요.
- 5. 작업 단위 크기 조정
  - 작업 단위를 너무 작게 쪼개지 않기: 애플리케이션이 아주 작은 작업 단위로 쪼개져 있다면 스레드 교체가 빈번해지고, 컨텍스트 스위칭 오버헤드가 커질 수 있음. -> 가능하면 적절히 크고 연속적인 작업 단위를 유지하는 것이 좋음.
- 6. 스케줄링 정책 이해
  - 스케줄링 알고리즘 이해: Linux나 Windows와 같은 운영체제는 특정 스케줄링 알고리즘을 사용해 스레드를 관리. CPU 바운드와 I/O 바운드 작업이 혼합된 애플리케이션이라면, 스케줄링 알고리즘이 CPU 시간을 어떻게 분배하는지 알고 작업 특성에 맞게 튜닝하는 것이 좋음.
  - 우선순위 기반 스케줄링 주의: 우선순위 기반 스케줄링을 사용할 경우, 우선순위가 낮은 작업은 CPU 시간을 얻기 어려워질 수 있으며 오버헤드가 증가할 수 있음. 우선순위의 균형을 유지하고, 필요한 경우 우선순위 상속(priority inheritance) 같은 방식을 활용.
- 7. 가상 스레드와 같은 경량 스레드 사용 (Java)
  - 가상 스레드 활용: Java 19 이상에서 제공되는 가상 스레드는 컨텍스트 스위칭 비용을 줄일 수 있는 경량 스레드 모델. 가상 스레드는 OS 스레드가 아닌 JVM 레벨에서 관리되므로, 스레드가 많아도 상대적으로 적은 자원으로 관리할 수 있음.
  - 적합한 상황에서 가상 스레드 사용: CPU 바운드 작업이 아닌 I/O 바운드 작업에서 가상 스레드를 사용할 때 특히 효과적. 가상 스레드의 장점을 이해하고, 적절한 상황에서 사용해야 컨텍스트 스위칭 오버헤드를 줄이는 데 도움을 줌.
- 8. 시스템 모니터링 및 프로파일링
  - 프로파일링 툴 사용: VisualVM, JProfiler, perf 같은 프로파일링 툴을 사용하여 컨텍스트 스위칭이 많이 발생하는 구간을 모니터링하면, 성능 병목을 파악하고 이를 최적화 가능
  - 컨텍스트 스위칭 횟수 모니터링: 컨텍스트 스위칭이 많이 발생하는 경우, 운영체제의 모니터링 도구(예: Linux의 vmstat나 pidstat)를 통해 스레드 상태와 스위칭 횟수를 추적하면 성능 최적화의 방향을 잡는 데 도움을 줌



<br>
<hr>
<br>

### 그래서 멀티 스레드 프로그래밍에서 컨텍스트 스위칭 오버헤드를 어떻게 판단하는데?

- 병렬, 동시성 처리를 자주 하는 작업에 대하여, 꾸준하게 모니터링을 진행하고 -> 이를 통해 CPU Resource가 어떻게 할당되는지 확인
- 처리율, throughput 확인이 필요함..


<br>
<hr>
<br>

# Atomic (CAS), Syncronized (lock), voilate, (FolkJoinPool, BlockingDeque, java.util.concurrent) - 10분

<br>
<hr>
<br>

### atomic

java의 AtomicInteger는 Cas 알고리즘을 따른다.

![image](https://github.com/DongGeon0908/java-cafe-java-deep-dive-2024/assets/50691225/dc70da37-6852-4316-a939-c4ba6a37e6e4)

Atomic, Adder, Accumulator 등의 기술 존재

![image](https://github.com/DongGeon0908/java-cafe-java-deep-dive-2024/assets/50691225/622cfa93-7ad1-4627-b4b9-9677d2e224bd)

**CAS(Compare-And-Swap) 알고리즘의 주요 특징**
- 비교 및 교체: 메모리 위치의 값을 특정 값과 비교하고, 같을 경우에만 새로운 값으로 교체하는 원자적 연산을 수행
- 낙관적 동시성 제어: 여러 스레드가 동시에 값을 수정하려 할 때, 실패한 스레드는 대기하지 않고 재시도할 수 있어 성능 향상에 기여
- ABA 문제: CAS는 값이 중간에 변경되었다가 다시 원래 값으로 돌아오는 경우를 처리하지 못하는 문제가 있으며, 이를 ABA 문제라고 지칭

**ABA Problem**
- 값의 재사용: 공유된 메모리 값이 A에서 B로 변경된 후 다시 A로 돌아오면, CAS 연산은 값이 변경되지 않았다고 오인 가능
- 데이터 일관성 문제: 중간에 다른 스레드가 값을 변경한 사실을 인지하지 못해, 데이터의 무결성이 손상될 수 있음
- 해결책: 버전 번호를 추가하거나 AtomicStampedReference와 같은 구조를 사용해 값뿐만 아니라 상태 변화를 추적하는 방식으로 문제를 해결 가능

<br>
<hr>
<br>


### synchronized

<img width="462" alt="image" src="https://github.com/user-attachments/assets/46be4d51-3a46-409d-9334-fb12727f0ce6">
- 해당 코드에 대한 스레드가 블록킹되어 사용되어짐.

- Java의 synchronized 키워드는 멀티스레드 환경에서 동기화를 통해 여러 스레드가 동시에 공유 자원에 접근하는 것을 제어하는 메커니즘이다. 이 키워드는 내부적으로 **모니터 락(Monitor Lock)**을 사용하여 구현되며, 하나의 스레드만 임계영역에 접근할 수 있도록 보장한다.

<img width="1010" alt="image" src="https://github.com/user-attachments/assets/e9ce3372-8ece-4bdd-8b8e-ec6538140fec">


**동작 원리**
- 모니터 락: synchronized 블록 또는 메서드에 진입할 때, 해당 객체의 모니터 락을 획득한다. 다른 스레드는 락이 해제될 때까지 대기한다.
- 임계영역 보호: synchronized는 특정 코드 블록이나 메서드 전체를 임계영역으로 설정하여, 한 번에 하나의 스레드만 이 영역을 실행할 수 있도록 한다. 이를 통해 데이터의 일관성을 보장한다.
- 락 해제: synchronized 블록이나 메서드를 벗어나면 자동으로 락이 해제되어 다른 대기 중인 스레드가 접근할 수 있다.
- 알고리즘 기반 설명
  - 모니터 락 기반: synchronized는 각 객체 또는 클래스에 대해 하나의 모니터 락을 사용한다. 인스턴스 메서드는 해당 객체에 대해, 클래스 메서드는 클래스 레벨에서 락을 걸어 동기화를 수행한다.
  - 락 획득 및 해제: 스레드는 synchronized 블록에 진입할 때 모니터 락을 획득하고, 블록이 끝나거나 예외가 발생해도 자동으로 락이 헤재한다.
  - 동기화 방식: synchronized는 크게 4가지 방식으로 사용한다. 인스턴스 메서드, 클래스(static) 메서드, 인스턴스 블록, 클래스(static) 블록 동기화 등.
- synchronized는 구현이 간단하고 직관적이지만, 성능 저하를 유발할 수 있으므로 필요한 최소한의 코드 영역에만 적용하는 것이 중요한다.


<br>
<hr>
<br>


### Java Monitor

**Java 모니터의 동작 원리**
- Java에서 **모니터(Monitor)**는 동기화와 **상호 배제(Mutual Exclusion)**를 제공하는 중요한 메커니즘으로, 객체 수준에서 스레드 간의 안전한 자원 접근을 보장
- 모든 Java 객체는 내부적으로 모니터를 가지고 있으며, 이를 통해 스레드가 동시에 공유 자원에 접근하는 것을 제어
- 모니터는 **뮤텍스(Mutex)**와 **조건 변수(Condition Variable)**로 구성되어 있으며, synchronized 키워드를 통해 동작

**주요 동작 과정**
- 락 획득 및 해제:
  - 스레드가 synchronized 블록이나 메서드에 진입하면 해당 객체의 모니터 락을 획득
  - 다른 스레드는 이 락이 해제될 때까지 대기해야 하며, 락을 해제하는 시점은 synchronized 블록을 벗어날 때
- Wait Set과 Entry Set:
  - Entry Set: 락을 기다리는 스레드들이 대기하는 공간. 락이 해제되면 Entry Set에 있는 스레드 중 하나가 선택되어 락을 획득
  - Wait Set: wait() 메서드를 호출한 스레드는 락을 포기하고 Wait Set으로 이동하여 특정 조건이 만족될 때까지 대기. notify()나 notifyAll()이 호출되면 Wait Set에 있는 스레드가 깨어남
- wait(), notify(), notifyAll():
  - wait(): 현재 스레드는 락을 해제하고 Wait Set에서 대기 상태로 들어감
  - notify(): Wait Set에서 대기 중인 스레드 중 하나를 깨움
  - notifyAll(): Wait Set에 있는 모든 스레드를 깨움

**모니터의 구조**
- Java 모니터의 구조는 아래와 같은 방식으로 동작
  - 락(Lock): 모니터의 문 역할을 하며, 한 번에 하나의 스레드만 락을 획득하고 모니터에 진입 가능
  - Entry Set: 락을 기다리는 스레드들이 대기하는 공간.
  - Wait Set: 조건이 충족되지 않아 대기 중인 스레드들이 모이는 공간.

**모니터 동작 그림**
```
+-----------------------+
|       Monitor         |
|  +-----------------+  |
|  |     Lock        |  |  <-- 락을 획득한 스레드만 임계영역에 진입 가능
|  +-----------------+  |
|  |                 |  |
|  |    Entry Set    |  |  <-- 락을 기다리는 스레드들이 대기
|  |   (Waiting for  |  |
|  |     Lock)       |  |
|  +-----------------+  |
|  |                 |  |
|  |    Wait Set     |  |  <-- 조건이 충족되지 않아 wait() 호출 후 대기 중인 스레드들
|  | (Waiting for    |  |
|  | notify/notifyAll)| |
|  +-----------------+  |
+-----------------------+
```

Lock: 모니터에 접근하려면 이 락을 먼저 획득해야 함.
Entry Set: 여러 스레드가 락을 얻기 위해 대기할 때 이곳에 머무름.
Wait Set: 특정 조건이 충족되지 않아 wait() 상태로 전환된 스레드들이 대기하는 공간.


<br>
<hr>
<br>


### volatile

<img width="587" alt="image" src="https://github.com/user-attachments/assets/230489d5-beb1-4b4c-a084-2760cc51bc8a">


**Java volatile의 동작 원리**
- Java에서 volatile 키워드는 **변수의 가시성(visibility)**을 보장하는 데 사용되며, 멀티스레드 환경에서 여러 스레드가 동시에 접근하는 변수의 값을 항상 메인 메모리에서 읽고 쓰도록 강제. 이를 통해 스레드 간의 데이터 일관성을 유지

<img width="859" alt="image" src="https://github.com/user-attachments/assets/f46443d5-88a8-471f-a765-70f4ac8fe5bb">


**주요 동작 원리**
- 메모리 가시성 보장:
  - volatile로 선언된 변수는 각 스레드의 캐시가 아닌 메인 메모리에서 직접 읽고 쓰여짐. 따라서 한 스레드가 변수 값을 수정하면, 다른 스레드는 즉시 그 변화를 볼 수 있음.
- 명령어 재정렬 방지:
  - volatile은 **명령어 재정렬(instruction reordering)**을 방지. 이는 컴파일러나 CPU가 최적화를 위해 코드 실행 순서를 변경하지 않도록 하여, 프로그램이 의도한 순서대로 실행되도록 보장.
- 원자성(Atomicity) 미보장:
  - volatile은 단순한 읽기/쓰기 작업에 대해서만 가시성을 보장하며, 복합 연산(예: count++)은 원자적으로 수행되지 않음. 이러한 경우에는 synchronized 블록이나 Atomic 클래스를 사용해야함.

```
public class VolatileExample {
    private volatile boolean flag = false;

    public void setFlagTrue() {
        flag = true;  // 메인 메모리에 즉시 반영됨
    }

    public void checkFlag() {
        while (!flag) {
            // flag가 true로 변경될 때까지 대기
        }
        System.out.println("Flag is now true.");
    }
}
```

> 위 예시에서 flag 변수는 volatile로 선언되어, 한 스레드가 flag = true로 변경하면 다른 스레드가 즉시 그 변화를 감지할 수 있음.


<br>
<hr>
<br>

### FolkJoinPool

- ForkJoinPool은 Java 7에서 도입된 병렬 처리 프레임워크로, 작업을 작은 단위로 분할하고 여러 스레드에 할당하여 병렬로 처리하는 데 최적화된 스레드 풀
- 이 풀은 분할 정복(Divide and Conquer) 알고리즘을 기반으로 하며, 특히 재귀적으로 작업을 분할하는 태스크를 처리하는 데 유용
- ForkJoinPool의 주요 특징
  - 작업 분할(Fork)과 병합(Join):
    - 큰 작업을 작은 **서브태스크(Subtask)**로 분할한 후, 각 서브태스크를 병렬로 실행. 모든 서브태스크가 완료되면 결과를 다시 합쳐 최종 결과를 생성
  - Work-Stealing 알고리즘:
    - 각 스레드는 자신의 작업 큐를 가지고 있으며, 큐에 작업이 없으면 다른 스레드의 큐에서 남은 작업을 훔쳐서 처리. 이를 통해 스레드 간의 부하를 균형 있게 유지하고, 유휴 상태의 스레드를 최소화

- ForkJoinTask:
  - ForkJoinPool은 두 가지 유형의 태스크인 RecursiveAction(결과가 없는 태스크)과 RecursiveTask(결과가 있는 태스크)를 사용하여 작업을 처리. 이들은 모두 ForkJoinTask 

```
ForkJoinPool forkJoinPool = new ForkJoinPool();
MyRecursiveTask task = new MyRecursiveTask(data);
Integer result = forkJoinPool.invoke(task);  // 병렬로 작업 수행
```

**장점**
- 효율적인 CPU 활용: 모든 CPU 코어를 최대한 활용하여 병렬 처리를 수행.
- 유연한 확장성: 작업 크기에 따라 자동으로 적절한 수준으로 분할 및 병합이 이루어짐.

**주의점**
- 작은 작업의 과도한 분할과 작업 훔치기에는 오버헤드가 발생할 수 있으므로, 적절한 임계값 설정이 필요합니다3.

<br>
<hr>
<br>

### ThreadLocal

> ThreadLocal은 Java에서 제공하는 클래스이며, 멀티스레드 환경에서 각 스레드가 독립적으로 변수를 관리할 수 있도록 도와준다. 이를 통해 **스레드 안전성(Thread-Safety)**을 보장하며, 각 스레드는 자신만의 고유한 값을 저장하고 사용할 수 있다.

<img width="921" alt="image" src="https://github.com/user-attachments/assets/dd8a318d-d6f6-4a3e-82c6-54a6aed298e5">


**ThreadLocal의 동작 원리**
- 스레드별 독립적인 변수 저장:
  - ThreadLocal은 각 스레드가 자신만의 로컬 변수를 가질 수 있게 한다. 즉, 여러 스레드가 동일한 ThreadLocal 객체를 참조하더라도, 각 스레드는 서로 다른 값을 저장하고 읽는다.
  - 이는 공유 자원에 대한 동기화 문제를 해결하는 데 유용하다. 예를 들어, Spring Security에서는 사용자 인증 정보를 ThreadLocal을 사용해 스레드마다 독립적으로 관리한다.
- ThreadLocalMap을 통한 저장:
  - 내부적으로, 각 스레드는 자신의 ThreadLocalMap이라는 데이터를 가지고 있다. 이 맵은 ThreadLocal 객체를 키로 사용하며, 해당 스레드에 맞는 값을 저장한다.
  - ThreadLocal.get() 메서드를 호출하면 현재 실행 중인 스레드의 ThreadLocalMap에서 값을 가져오고, set() 메서드를 통해 값을 설정한다.
- 주요 메서드:
  - set(T value): 현재 스레드의 로컬 변수에 값을 저장한다.
  - get(): 현재 스레드의 로컬 변수 값을 반환한다.
  - remove(): 현재 스레드의 로컬 변수 값을 삭제한다. 이는 메모리 누수를 방지하기 위해 반드시 호출해야 한다.

**ThreadLocal 사용 예시**
```
public class ThreadLocalExample {
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

    // 이 코드에서 두 개의 스레드는 각각 독립적인 값(100과 200)을 가지며, 서로 영향을 주지 않는다. 
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            threadLocal.set(100);
            System.out.println("Thread 1: " + threadLocal.get());
        });

        Thread thread2 = new Thread(() -> {
            threadLocal.set(200);
            System.out.println("Thread 2: " + threadLocal.get());
        });

        thread1.start();
        thread2.start();
    }
}
```

**ThreadLocal의 주요 활용 사례**
- 사용자 인증 정보 전파: Spring Security에서는 각 사용자 세션마다 인증 정보를 관리하기 위해 ThreadLocal을 사용한다.
- 트랜잭션 컨텍스트 전파: 트랜잭션 매니저는 트랜잭션 컨텍스트를 전파하는 데 ThreadLocal을 사용하여 각 스레드가 고유한 트랜잭션 상태를 유지할 수 있도록 한다.
- 스레드 안전 데이터 보관: 멀티스레드 환경에서 동기화 없이도 안전하게 데이터를 저장하고 사용할 수 있다.

**주의 사항**
- 메모리 누수 위험: 특히 쓰레드 풀 환경에서는 remove() 메서드를 통해 반드시 데이터를 삭제해야 한다. 그렇지 않으면 재사용되는 스레드가 이전 작업의 데이터를 참조할 수 있어 예상치 못한 결과를 초래할 수 있다.


<br>
<hr>
<br>
<br>
<br>
<br>
<br>
<br>

# Tomcat 네트워크 요청을 받아서, 스레드를 할당받고, 이게 스프링까지 넘어와서 어떤식으로 스레드가 처리되는지?

Tomcat의 스레드가 소켓으로 네트워크 요청을 받아들여 이를 Spring 애플리케이션까지 전달하면서 작업하는 과정에 대해 더욱 깊이 있게 설명해 보겠습니다. 이 과정에서는 Tomcat의 네트워크 I/O 처리부터 Spring의 애플리케이션 레벨에서의 요청 처리까지 다루게 됩니다.

---

![image](https://github.com/user-attachments/assets/d4eaddbf-0d76-450d-a93d-a08fbeb342ae)

![image](https://github.com/user-attachments/assets/9c3b44c8-c1ff-4d89-81a0-dcf3e433c34c)

![image](https://github.com/user-attachments/assets/b6fe4306-6287-49fd-869d-1bba806d9124)

![image](https://github.com/user-attachments/assets/9806b34e-446c-4064-91d4-9905158dbec0)


### 1. 소켓 연결 및 요청 수신 (Tomcat Connector 레벨)

   - **소켓 연결**: 클라이언트가 서버에 HTTP 요청을 보내면, 이 요청은 서버에서 열려 있는 포트(예: 포트 8080)에 연결됩니다. Tomcat의 `Connector`는 서버 포트에 바인딩되어 있으며, 클라이언트가 연결을 시도하면 이를 소켓으로 받아들입니다.
   - **네트워크 I/O 스레드**: Tomcat의 `Connector`는 **NIO(Non-blocking I/O)** 모델을 사용하여 다중 요청을 비동기적으로 수신할 수 있습니다. NIO 모델에서는 **Acceptor 스레드**가 소켓 채널을 지속적으로 모니터링하며, 클라이언트로부터 새 연결이 발생할 때 이를 감지합니다.
   - **소켓 수락 및 스레드 할당**: 클라이언트 연결이 생기면, `Connector`의 Acceptor 스레드는 이 소켓을 수락하고, **스레드 풀에서 워커 스레드**를 할당합니다. 이때, 새롭게 할당된 워커 스레드는 소켓에서 데이터를 읽어올 준비를 합니다.

### 2. 워커 스레드의 소켓 읽기 및 HTTP 요청 파싱
   - **소켓의 비동기 데이터 읽기**: 할당된 워커 스레드는 클라이언트가 보낸 데이터를 소켓으로부터 읽습니다. Tomcat은 이 작업을 비동기적으로 수행하여 **처리 시간이 긴 요청에 대해 블로킹되지 않도록** 설계되어 있습니다.
   - **HTTP 파싱**: 워커 스레드는 읽어 들인 데이터를 HTTP 형식에 맞춰 파싱합니다. 이 과정에서 `GET`, `POST`와 같은 HTTP 메서드, URL, 헤더, 본문 등의 정보를 추출하여, 이 정보들을 Tomcat의 내부 `Request` 객체에 저장합니다. **Request와 Response 객체는 각 요청마다 개별적으로 생성되며**, 이를 통해 다중 요청 처리 시 스레드 간의 데이터 충돌이 방지됩니다.

### 3. 서블릿 할당 및 Pipeline 처리 (Catalina 엔진)
   - **Pipeline과 Valve 패턴**: Tomcat의 핵심 엔진인 `Catalina`는 **Pipeline**과 **Valve** 구조를 통해 요청을 전처리합니다. `Valve`는 필터와 유사한 역할을 하며, 인증, 인가, 로깅과 같은 작업을 수행합니다. 각 Valve는 워커 스레드에서 순차적으로 실행되며, Valve의 처리가 끝나면 요청이 다음 단계로 넘어갑니다.
   - **Wrapper 객체와 서블릿 매핑**: `Catalina` 엔진은 `Context`와 `Wrapper` 객체를 통해 요청을 서블릿에 매핑합니다. 최종적으로 요청이 `Wrapper` 객체에 도달하면, Tomcat은 서블릿을 찾아서 요청을 해당 서블릿으로 전달합니다. Spring 기반 애플리케이션에서는 이때 `DispatcherServlet`으로 요청이 라우팅됩니다.

### 4. `DispatcherServlet`에서의 스레드 처리 (Spring 진입점)
   - **스레드 유지**: Tomcat에서 할당된 워커 스레드는 `DispatcherServlet`까지 그대로 전달되어, 스레드는 변경 없이 애플리케이션 레벨까지 동일하게 사용됩니다. `DispatcherServlet`은 모든 요청을 중앙에서 관리하며, 요청을 적절한 컨트롤러에 매핑합니다.
   - **Handler Mapping**: `DispatcherServlet`은 URL 패턴과 HTTP 메서드에 따라 적절한 컨트롤러를 찾기 위해 **HandlerMapping**을 사용합니다. 스레드는 이 HandlerMapping 로직을 거쳐 적합한 컨트롤러 메서드에 요청을 전달합니다.
   - **Handler Adapter**: 매핑된 핸들러가 결정되면, `DispatcherServlet`은 **Handler Adapter**를 사용해 컨트롤러의 메서드를 호출합니다.

### 5. 컨트롤러 및 비즈니스 로직 처리
   - **Service 계층 호출 및 데이터 처리**: 컨트롤러 메서드는 요청에 필요한 비즈니스 로직을 수행하기 위해 **Service 계층**을 호출합니다. 이때 서비스 계층에서는 데이터베이스 접근이나 외부 API 호출이 포함될 수 있습니다.
   - **트랜잭션 관리**: Spring에서 `@Transactional` 어노테이션을 사용하는 경우, 동일한 스레드 내에서 트랜잭션 경계를 형성하여 데이터 일관성을 보장합니다.
   - **스레드가 지속적으로 처리**: 요청에 할당된 스레드는 서비스 로직을 수행하는 동안에도 계속 유지되며, 서비스 작업이 완료되면 해당 데이터를 컨트롤러로 반환합니다.

### 6. 응답 데이터 생성 및 뷰 렌더링
   - **ModelAndView 반환**: 컨트롤러는 최종 결과를 `ModelAndView` 객체에 담아 반환합니다. 이 객체는 사용자에게 보여줄 뷰와 뷰에 전달할 모델 데이터를 포함하고 있습니다.
   - **View Resolver**: `DispatcherServlet`은 반환된 `ModelAndView`를 통해 적절한 뷰를 찾기 위해 **View Resolver**를 사용합니다. 뷰가 JSP, Thymeleaf와 같은 템플릿 엔진일 경우 HTML 응답을 생성하고, REST API라면 JSON으로 직렬화된 데이터를 반환합니다.
   - **Response 객체에 쓰기**: 최종적으로 워커 스레드는 렌더링된 응답을 `Response` 객체에 작성하며, 이 `Response`는 클라이언트에게 전송될 준비를 합니다.

### 7. 응답 전송 및 스레드 반환
   - **응답 완료 후 스레드 반환**: 모든 작업이 완료되면, 워커 스레드는 클라이언트에게 응답을 전송합니다. 이 작업이 끝난 후에 Tomcat은 해당 스레드를 스레드 풀에 반환하여 다음 요청을 처리할 수 있도록 합니다.
   - **스레드 재사용**: 스레드는 특정 요청이 아닌 스레드 풀을 통해 관리되므로, 하나의 요청이 완료되면 다른 요청을 처리하기 위해 스레드가 재사용됩니다. 이를 통해 Tomcat은 메모리 사용을 최적화하며, 동시 요청에 대응할 수 있는 성능을 유지합니다.

---

### 비동기 요청 처리 시의 스레드 동작

특정 상황에서는 비동기 처리를 통해 메인 워커 스레드를 반환하고, 별도의 스레드에서 처리가 이루어지기도 합니다.

   - **Callable과 DeferredResult 사용**: 비동기 처리를 위해 `Callable`과 `DeferredResult` 같은 비동기 처리 전략을 사용할 수 있습니다. 이 경우 `DispatcherServlet`은 워커 스레드를 반환하고, 다른 스레드에서 작업을 처리할 수 있도록 합니다.
   - **별도의 스레드 풀**: Spring에서는 비동기 작업을 위해 별도의 스레드 풀을 사용할 수 있습니다. 요청이 비동기적으로 처리되는 동안 워커 스레드는 풀로 반환되고, 작업 완료 시 다시 새 스레드가 요청 응답을 마무리합니다.

---

![image](https://github.com/user-attachments/assets/800ab546-4303-446e-9bd4-7702d5dbee66)
