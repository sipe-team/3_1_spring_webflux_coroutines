package com.sipe.week4

import kotlinx.coroutines.delay

/** 코드 흐름
1.	UserService.findUser 호출:
•	findUser 메서드는 Continuation을 받아 코루틴의 중단 및 재개 상태를 관리합니다.
•	초기 호출 시 Continuation이 null이므로 새로운 상태 머신 객체(FindUserContinuation)가 생성됩니다.
2.	FindUserContinuation 객체 생성:
•	내부적으로 resumeWith 메서드를 통해 재개 시 상태를 업데이트합니다.
•	label 값을 통해 진행 상태를 관리:
•	label == 0: 프로필 가져오기 단계
•	label == 1: 이미지 가져오기 단계
3.	findUser 상태에 따라 작업 분기:
•	label == 0: UserProfileRepository.findProfile을 호출하여 프로필 데이터를 가져옴.
•	label == 1: UserImageRepository.findImage를 호출하여 이미지 데이터를 가져옴.
•	label == 2: 모든 데이터를 가져온 후 UserDto를 생성해 반환.
4.	재개 (Resume):
•	findProfile 및 findImage에서 데이터를 준비한 후 resumeWith을 호출하여 다음 단계로 넘어갑니다.
•	resumeWith 호출 시:
•	label 값을 업데이트.
•	findUser를 재귀적으로 호출하여 다음 단계를 처리.
 */
class ContinuationExample {
}

suspend fun main() {
    val service = UserService()
    println(service.findUser(1L, null))
}

interface Continuation {
    // 라벨을 가지고 있을 것

    // suspend fun에서 불릴 애들. callback
    suspend fun resumeWith(data: Any?)
}

class UserService {
    private val userProfileRepository = UserProfileRepository()
    private val userImageRepository = UserImageRepository()

    private abstract class FindUserContinuation : Continuation {
        var label = 0
        var profile: Profile? = null
        var image: Image? = null
    }

    suspend fun findUser(userId: Long, continuation: Continuation?): UserDto {
        // state machine
        val sm = continuation as? FindUserContinuation ?: object : FindUserContinuation() {
            // 일종의 재귀함수
            override suspend fun resumeWith(data: Any?) {
                when (label) {
                    0 -> {
                        profile = data as Profile
                        label = 1
                    }
                    1 -> {
                        image = data as Image
                        label = 2
                    }
                }
                findUser(userId, this)
            }
        }

        when (sm.label) {
            0 -> {
                // 0단계 - 초기 시작
                println("프로필을 가져오겠습니다")
                userProfileRepository.findProfile(userId, sm)
            }
            1 -> {
                // 1단계 - 1차 중단 후 재시작
                println("이미지를 가져오겠습니다")
                userImageRepository.findImage(sm.profile!!, sm)
            }
        }
        // 2단계 - 2차 중단 후 재시작
        return UserDto(sm.profile!!, sm.image!!)
    }
}

class UserProfileRepository {
    suspend fun findProfile(userId: Long, continuation: Continuation){
        delay(100L)
        continuation.resumeWith(Profile())
    }
}

class UserImageRepository {
    suspend fun findImage(profile: Profile, continuation: Continuation){
        delay(100L)
        continuation.resumeWith(Image())
    }
}

data class Profile(
    val value: String = ""
)

data class Image(
    val value: String = ""
)

data class UserDto (
    val profile: Profile,
    val image: Image
)