package com.sipe.week5.domain.member.domain

import com.sipe.week5.domain.common.BaseEntity
import com.sipe.week5.domain.todo.domain.TodoEntity
import jakarta.persistence.*
import kotlin.reflect.full.isSubclassOf

@Entity
class Member(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	val id: Long = -1,
	val loginId: String,
	var username: String,
	var password: String,
	var studyGoal: String? = null,
	@Enumerated(EnumType.STRING)
	var role: MemberRole = MemberRole.USER,
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	val todos: MutableList<TodoEntity> = mutableListOf(),
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
