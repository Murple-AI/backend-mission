package app.boboc.chatserver.entity

import app.boboc.chatserver.data.CountryCode
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("user_phone_number")
data class UserPhoneNumberEntity(
    @Id
    val id: Long? = null,
    val userId: Long,
    val label: String,
    val countryCode: CountryCode,
    val phoneNumber: String,
    val isVerified: Boolean = false,
    val isDeleted: Boolean = false,
    val updatedAt: Instant = Instant.now(),
    val createdAt: Instant = Instant.now()
)
