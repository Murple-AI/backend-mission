package app.boboc.chatserver.repository

import app.boboc.chatserver.entity.UserPhoneNumberEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserPhoneNumberRepository: CoroutineCrudRepository<UserPhoneNumberEntity, Long> {
    suspend fun findByUserIdAndDeletedFalseOrderById(userId: Long): List<UserPhoneNumberEntity>
}
