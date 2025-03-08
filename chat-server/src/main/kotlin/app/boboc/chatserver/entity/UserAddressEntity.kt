package app.boboc.chatserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("user_address")
data class UserAddressEntity(
    @Id
    val id: Long? = null,
    val userId: Long,
    val label: String,
    val address: String,
    val isDeleted: Boolean = false,
    val updatedAt: Instant = Instant.now(),
    val createdAt: Instant = Instant.now(),
)