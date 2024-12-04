import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("todo")
data class Todo(
    @Id
    val id: Long? = null,
    val title: String,
    val description: String,
    val status: TodoStatus
)

enum class TodoStatus {
    PENDING, IN_PROGRESS, COMPLETED
}