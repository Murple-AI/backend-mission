package app.boboc.chatserver.repository

import app.boboc.chatserver.entity.UserPhoneNumberEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserPhoneNumberRepository: CoroutineCrudRepository<UserPhoneNumberEntity, Long> {
    suspend fun findAllByUserIdAndDeletedFalseOrderById(userId: Long): List<UserPhoneNumberEntity>
    suspend fun findByIdAndUserIdAndDeletedFalse(userId: Long, id: Long): UserPhoneNumberEntity?
    suspend fun countByUserIdAndDeletedFalse(userId: Long): Long
}
