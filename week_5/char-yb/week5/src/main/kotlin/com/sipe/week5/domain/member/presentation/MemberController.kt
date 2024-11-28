package com.sipe.week5.domain.member.presentation

import com.sipe.week5.domain.member.application.MemberService
import com.sipe.week5.domain.member.dto.response.FindOneMemberResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
	private val memberService: MemberService,
) {
	@GetMapping("/me")
	fun memberFindMe(): FindOneMemberResponse = memberService.findMemberMe()
}
