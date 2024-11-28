package com.sipe.week5.global.util.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/* Logger 인스턴스
 * Logger 인스턴스를 생성하는 함수
 * inline 함수로 선언하여 Logger 인스턴스를 생성하는 코드를 호출하는 위치에 삽입
 * reified는 코틀린에서 제네릭 타입 파라미터를 실체화(reify)하는 키워드
 * lazy를 사용하여 로그 인스턴스가 필요할 때까지 로그 객체의 생성을 지연
 * 일반적으로, 제네릭 타입은 런타임에 지워지는데, 이를 타입 소거(type erasure)라고 한다. 즉, 런타임에는 제네릭 타입 정보를 알 수 없다.

* 참고 링크
* https://cheese10yun.github.io/kotlin-pattern/#null
 * 사용 예시
 * private val log by logger()
 * log.info("Hello, World!")
 * 포인트 정리
효율적인 자원 사용: lazy를 사용함으로써 로거의 초기화를 실제 로깅이 필요한 시점까지 지연. 이는 자원을 효율적으로 사용
코드 중복 감소: logger() 확장 함수를 사용하면 모든 클래스에서 동일한 로깅 구성을 쉽게 재사용성 용이
유지보수의 용이성: 로그 인스턴스 생성 코드를 한 곳에 집중시키므로, 로거 설정을 변경할 때 다수의 클래스를 수정할 필요가 없다.
 */
inline fun <reified T> T.logger(): Lazy<Logger> = lazy { LoggerFactory.getLogger(T::class.java) }
