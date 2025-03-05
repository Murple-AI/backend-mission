package app.boboc.chatserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("user_phone_number")
data class UserPhoneNumberEntity(
    @Id
    val id: Long? = null,
    val userId: Long,
    val label: String,
    val countryCode: String,
    val phoneNumber: String,
    val isDeleted: Boolean = false,
    val updatedAt: Instant = Instant.now(),
    val createdAt: Instant = Instant.now()
)
