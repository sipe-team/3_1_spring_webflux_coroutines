package com.sipe.week5.domain.member.domain

import com.sipe.week5.domain.common.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import kotlin.reflect.full.isSubclassOf

@Table
class Member(
	@Id
	@Column("member_id")
	val id: Long = 0L,
	@Column("login_id")
	val loginId: String,
	@Column("username")
	var username: String,
	@Column("password")
	var password: String,
	@Column("role")
	var role: MemberRole = MemberRole.USER,
) : BaseEntity() {
	// Proxy 객체 고려하여 equals Override, https://zins.tistory.com/19
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Member) return false
		if (!compareClassesIncludeProxy(other)) return false
		if (id != other.id) return false
		return true
	}

	private fun compareClassesIncludeProxy(other: Any) =
		this::class.isSubclassOf(other::class) ||
			other::class.isSubclassOf(this::class)

	override fun hashCode(): Int = id.hashCode()
}
