package com.sipe.week5.domain.todo.domain

import com.sipe.week5.domain.common.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import kotlin.reflect.full.isSubclassOf

@Table("todo")
class TodoEntity(
	@Id
	@Column("todo_id")
	val id: Long = -1,
	val title: String,
	val content: String,
	val dueDate: LocalDate,
	@Column("status")
	val status: TodoStatus = TodoStatus.TODO,
	@Column("member_id")
	val memberId: Long
) : BaseEntity() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is TodoEntity) return false
		if (!compareClassesIncludeProxy(other)) return false
		if (id != other.id) return false
		return true
	}

	private fun compareClassesIncludeProxy(other: Any) =
		this::class.isSubclassOf(other::class) ||
			other::class.isSubclassOf(this::class)

	override fun hashCode(): Int = id.hashCode()
}
