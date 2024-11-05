## 1주차

- Thread, Runnable, Callable, ExecutorService, Async, CompletableFure, ThreadLocal
- Atomic (CAS), Syncronized (lock), voilate, FolkJoinPool, BlockingDeque

---

## Thread

스레드는 프로세스 내에서 실행되는 작은 단위.
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

```kotlin
public void start() {
    synchronized (this) {
        // zero status corresponds to state "NEW".
        if (holder.threadStatus != 0)
            throw new IllegalThreadStateException();
        start0();
    }
}
```

예제 코드 중에 Thread start 메서드를 까보았다.
holder? 이게 대체 뭘까??

```kotlin
// Additional fields for platform threads.
    // All fields, except task, are accessed directly by the VM.
    private static class FieldHolder {
        final ThreadGroup group;
        final Runnable task;
        final long stackSize;
        volatile int priority;
        volatile boolean daemon;
        volatile int threadStatus;

        FieldHolder(ThreadGroup group,
                    Runnable task,
                    long stackSize,
                    int priority,
                    boolean daemon) {
            this.group = group;
            this.task = task;
            this.stackSize = stackSize;
            this.priority = priority;
            if (daemon)
                this.daemon = true;
        }
    }
    private final FieldHolder holder;
```

스레드는 New, Runnable, Waiting, Timed Waiting, Terminated 5가지 상태가 있다.

Thread에 대한 FieldHolder 인스턴스가 선언된 것을 볼 수 있다.
(volatile은 휘발성인 키워드를 의미하는 거 같다.)
그렇다면 threadStatus가 0인 경우에만 스레드가 실행되는 것을 확인해볼 수 있다.

실행 가능한 상태가 아니라면 IllegalThreadStateException 예외를 발생시킨다.

```java
private native void start0();
```

실행되는 start0는 어떤 메서드인가??
이 메서드는 JVM에 의해서 호출된다. (native)
생성된 스레드 객체를 스케줄링이 가능한 상태로 전환하도록 JVM에 지시를 한다.

스케줄링에 의해서 스레드가 선택되면 JVM에 의해 run 메서드가 호출된다.
내부적으로 run을 호출하고, 스레드의 상태 역시 Runnable로 바뀌게 된다.
상태가 Runnable로 바뀌기에 start는 1번만 호출 가능하다.

여기서 native란??

- java native interface (JNI)의 기능
- java는 JVM 위에서 동작하기에 운영체제에 상관없이 동작 가능하다.
- 하지만 모든 운영체제의 모든 기능을 JVM이 담지 못하기 위해 이를 위해서 운영 체제의 고유 기능을 java로 해결하는 것이 아닌 운영체제가 구현된 언어 (C / C++)로 해결할 수 있게 만든다.

---

## Runnable

Runnable 인터페이스는 1개의 메서드만을 갖는 함수형 인터페이스이다.
스레드를 구현하기 위한 템플릿에 해당한다.

```
public class Thread implements Rannable {
    ...
}
```

Thread 클래스는 반드시 run 메서드를 구현해야 하며, Thread 클래스가 Runnable를 구현하고 있기 때문이다.

```kotlin
@Test
fun runnable() {
	val runnable = Runnable {
		println("Thread: ${Thread.currentThread().name} is running")
	}
	val thread = Thread(runnable)
	thread.start()
	println("Hello: ${Thread.currentThread().name}")
}

// Hello: Test worker
// Thread: Thread-4 is running
```

위와 같이 Runnable에 대한 run 메서드를 override 하여 사용할 수 있다.

---


[ Thread와 Runnable 비교 ]
Runnable은 익명 객체 및 람다로 사용할 수 있지만, Thread는 별도의 클래스를 만들어야 한다는 점에서 번거롭다. 또한 Java에서는 다중 상속이 불가능하므로 Thread 클래스를 상속받으면 다른 클래스를 상속받을 수 없어서 좋지 않다. 또한 Thread 클래스를 상속받으면 Thread 클래스에 구현된 코드들에 의해 더 많은 자원(메모리와 시간 등)을 필요로 하므로 Runnable이 주로 사용된다.

 
Ref: https://mangkyu.tistory.com/258 망나니개발자

## Callable
기존의 Runnable 인터페이스는 결과를 반환할 수 없다는 한계점이 있다.
반환값을 얻으려면 공용 메모리나 파이프를 사용해야 하는데, 이러한 작업은 번거롭고
제네릭을 사용해 return 받을 수 있는 `Callable`이 추가되었다.


```kotlin
    @Test
    fun callable_void() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = Callable<Void> {
            // Thread: pool-1-thread-1 is running
            println("Thread: ${Thread.currentThread().name} is running")
            null
        }

        executorService.submit(callable)
        executorService.shutdown()
    }

    @Test
    fun callable_String() {
        val executorService = Executors.newSingleThreadExecutor()

        val callable = Callable { "Thread: " + Thread.currentThread().name }

        executorService.submit(callable)
        executorService.shutdown()
    }
```

---
## ExecutorService


---
## Async


---
## CompletableFure


---
## ThreadLocal



