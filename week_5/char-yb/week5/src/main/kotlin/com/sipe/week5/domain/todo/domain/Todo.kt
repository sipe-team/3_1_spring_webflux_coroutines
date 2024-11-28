package com.sipe.week5.domain.todo.domain

import com.sipe.week5.domain.common.BaseEntity
import com.sipe.week5.domain.member.domain.Member
import jakarta.persistence.*
import java.time.LocalDateTime
import kotlin.reflect.full.isSubclassOf

@Entity
class Todo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    val id: Long = -1,
    val title: String,
    val content: String,
    val dueDate: LocalDateTime,
    @Enumerated(EnumType.STRING)
    val status: TodoStatus = TodoStatus.TODO,
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Todo) return false
        if (!compareClassesIncludeProxy(other)) return false
        if (id != other.id) return false
        return true
    }

    private fun compareClassesIncludeProxy(other: Any) =
        this::class.isSubclassOf(other::class) ||
                other::class.isSubclassOf(this::class)

    override fun hashCode(): Int = id.hashCode()
}
