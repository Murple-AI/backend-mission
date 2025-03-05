package app.boboc.chatserver.entity

import app.boboc.chatserver.data.GenderType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("user_info")
data class UserEntity(
    @Id
    val id: Long? = null,
    val name: String,
    val email: String,
    val gender: GenderType,
    val age: Int,
    val isDeleted: Boolean = false,
    val updatedAt: Instant = Instant.now(),
    val createdAt: Instant = Instant.now()
)
