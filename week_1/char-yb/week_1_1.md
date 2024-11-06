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

## Executor

동시 여러 요청을 처리해야 하는 경우 매번 스레드를 생성하는 것은 비효율적이다. 그래서 스레드를 미리 만들어두고 재사용하기 위한 Thread Pool이 등장하게 되는데, Executor 인터페이스는 스레드 풀의 구현을 위한 인터페이스이다. 이러한 Executor 인터페이스를 간단히 정리하면

- 등록된 작업(Runnable)을 실행하기 위한 인터페이스
- 작업 등록과 작업 실행 중에서 작업 실행만을 책임진다.

SOLID 원칙 중 I에 해당되는 인터페이스 분리 원칙에 맞는 등록된 작업에 대한 실행만을 수행하는 책임만 존재하며, 존재하는 Runnable을 실행하는 메서드만 가지고 있다.

```java
public interface Executor {

   void execute(Runnable command);

}
```

Executor 인터페이스는 개발자들이 해당 작업의 실행과 스레드의 사용 및 스케줄링 등등 다양한 작업에 대한 것들을 벗어나게 도와준다.

```kotlin
class ExecutorTest {

    @Test
    fun executorRun() {
        val runnable = Runnable {
            // Thread: Test worker
            println("Thread: ${Thread.currentThread().name}")
        }

        val executor: Executor = RunExecutor()
        executor.execute(runnable)
    }

    class RunExecutor : Executor {
        override fun execute(command: Runnable) {
            command.run()
        }
    }

    @Test
    fun executorStart() {
        val runnable =
            Runnable { println("Thread: " + Thread.currentThread().name) }

        val executor: Executor = StartExecutor()
        executor.execute(runnable)
    }

    class StartExecutor : Executor {
        override fun execute(command: Runnable) {
            Thread(command).start()
        }
    }
}
```

코드에서는 메인 스레드에서 실행되므로 override된 execute 메서드를 start 메서드를 통해 실행하면 된다.

---

## ExecutorService

ExecutorService는 작업(Runnable, Callable) 등록을 위한 인터페이스이며, ExecutorService는 Executor를 상속받아서 작업에 대한 등록뿐만 아니라 실행을 위한 `책임`도 갖고 있다. 그래서 스레드 풀은 기본적으로 ExecutorService 인터페이스를 구현한다. 대표적으로 ThreadPoolExecutor가 ExecutorService의 구현체인데, ThreadPoolExecutor 내부에 있는 Blocking Queue에 작업들을 등록해둔다.

같은 크기의 스레드 풀이 있다고 가정하면,  각각의 스레드는 작업들을 할당받아 처리하는데, 만약 사용 가능한 스레드가 없다면 작업은 Blocking Queue에서 계속 대기하게 된다. 그러다가 스레드가 작업을 끝내면 다음 작업을 할당받게 되는 것이다.
- 라이프사이클 관리를 위한 기능들
- 비동기 작업을 위한 기능들

<br />

#### 라이프사이클 관리를 위한 기능들
ExecutorService는 Executor의 상태 확인과 작업 종료 등 라이프사이클 관리를 위한 메소드들을 제공하고 있다. 
 
1. shutdown
- 새로운 작업들을 더 이상 받아들이지 않음
- 호출 전에 제출된 작업들은 그대로 실행이 끝나고 종료됨(Graceful Shutdown)


2. shutdownNow
- shutdown 기능에 더해 이미 제출된 작업들을 인터럽트시킴
- 실행을 위해 대기중인 작업 목록(`List<Runnable>`)을 반환함


3. isShutdown
- Executor의 shutdown 여부를 반환함


4. isTerminated
- shutdown 실행 후 모든 작업의 종료 여부를 반환함

5. awaitTermination
- shutdown 실행 후, 지정한 시간 동안 모든 작업이 종료될 때 까지 대기함
- 지정한 시간 내에 모든 작업이 종료되었는지 여부를 반환함

#### 비동기 작업을 위한 기능들
ExecutorService는 Runnable과 Callbale을 작업으로 사용하기 위한 메소드를 제공한다. 동시에 여러 작업들을 실행시키는 메소드도 제공하고 있는데, 비동기 작업의 진행을 추적할 수 있도록 Future를 반환한다. 반환된 Future들은 모두 실행된 것이므로 반환된 isDone은 true이다. 하지만 작업들은 정상적으로 종료되었을 수도 있고, 예외에 의해 종료되었을 수도 있으므로 항상 성공한 것은 아니다. 이러한 ExecutorService가 갖는 비동기 작업을 위한 메소드들을 정리하면 다음과 같다.

1. submit
- 실행할 작업들을 추가하고, 작업의 상태와 결과를 포함하는 Future를 반환함
- Future의 get을 호출하면 성공적으로 작업이 완료된 후 결과를 얻을 수 있음

2. invokeAll
- 모든 결과가 나올 때 까지 대기하는 블로킹 방식의 요청
- 동시에 주어진 작업들을 모두 실행하고, 전부 끝나면 각각의 상태와 결과를 갖는 `List<Future>`을 반환함

3. invokeAny
- 가장 빨리 실행된 결과가 나올 때 까지 대기하는 블로킹 방식의 요청
- 동시에 주어진 작업들을 모두 실행하고, 가장 빨리 완료된 하나의 결과를 Future로 반환받음


ExecutorService의 구현체로는 AbstractExecutorService가 있는데, ExecutorService의 메소드들(submit, invokeAll, invokeAny)에 대한 기본 구현들을 제공한다.

```java
public abstract class AbstractExecutorService implements ExecutorService {
    public AbstractExecutorService() {}

    public Future<?> submit(Runnable task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<Void> ftask = newTaskFor(task, null);
        execute(ftask);
        return ftask;
    }

    public <T> Future<T> submit(Runnable task, T result) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task, result);
        execute(ftask);
        return ftask;
    }

    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task);
        execute(ftask);
        return ftask;
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException {
        try {
            return doInvokeAny(tasks, false, 0);
        } catch (TimeoutException cannotHappen) {
            assert false;
            return null;
        }
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                           long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        return doInvokeAny(tasks, true, unit.toNanos(timeout));
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException {
        if (tasks == null)
            throw new NullPointerException();
        ArrayList<Future<T>> futures = new ArrayList<>(tasks.size());
        try {
            for (Callable<T> t : tasks) {
                RunnableFuture<T> f = newTaskFor(t);
                futures.add(f);
                execute(f);
            }
            for (int i = 0, size = futures.size(); i < size; i++) {
                Future<T> f = futures.get(i);
                if (!f.isDone()) {
                    try { f.get(); }
                    catch (CancellationException | ExecutionException ignore) {}
                }
            }
            return futures;
        } catch (Throwable t) {
            cancelAll(futures);
            throw t;
        }
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                         long timeout, TimeUnit unit)
        throws InterruptedException {
        if (tasks == null)
            throw new NullPointerException();
        final long nanos = unit.toNanos(timeout);
        final long deadline = System.nanoTime() + nanos;
        ArrayList<Future<T>> futures = new ArrayList<>(tasks.size());
        int j = 0;
        timedOut: try {
            for (Callable<T> t : tasks)
                futures.add(newTaskFor(t));

            final int size = futures.size();

            // Interleave time checks and calls to execute in case
            // executor doesn't have any/much parallelism.
            for (int i = 0; i < size; i++) {
                if (((i == 0) ? nanos : deadline - System.nanoTime()) <= 0L)
                    break timedOut;
                execute((Runnable)futures.get(i));
            }

            for (; j < size; j++) {
                Future<T> f = futures.get(j);
                if (!f.isDone()) {
                    try { f.get(deadline - System.nanoTime(), NANOSECONDS); }
                    catch (CancellationException | ExecutionException ignore) {}
                    catch (TimeoutException timedOut) {
                        break timedOut;
                    }
                }
            }
            return futures;
        } catch (Throwable t) {
            cancelAll(futures);
            throw t;
        }
        // Timed out before all the tasks could be completed; cancel remaining
        cancelAll(futures, j);
        return futures;
    }
```
invokeAll은 최대 쓰레드 풀의 크기만큼 작업을 동시에 실행시킨다. 그러므로 쓰레드가 충분하다면 동시에 실행되는 작업들 중에서 가장 오래 걸리는 작업만큼 시간이 소요된다. 하지만 만약 쓰레드가 부족하다면 대기되는 작업들이 발생하므로 가장 오래 걸리는 작업의 시간에 더해 추가 시간이 필요하다.

invokeAny는 가장 빨리 끝난 작업 결과만을 구하므로, 동시에 실행한 작업들 중에서 가장 짧게 걸리는 작업만큼 시간이 걸린다. 또한 가장 빠르게 처리된 작업 외의 나머지 작업들은 완료되지 않았으므로 cancel 처리되며, 작업이 진행되는 동안 작업들이 수정되면 결과가 정의되지 않는다.

이런 점에서 진행되는 작업되는 과정에서 트랜잭션에 대한 격리성이 어긋나면 결과가 정의되지 않기에 정합성이 어긋나지 않을까싶다.

---

## Async

---

## CompletableFure

---

## ThreadLocal
