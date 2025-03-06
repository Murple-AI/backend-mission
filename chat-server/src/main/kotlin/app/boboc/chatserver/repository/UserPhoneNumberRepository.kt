package app.boboc.chatserver.repository

import app.boboc.chatserver.entity.UserPhoneNumberEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserPhoneNumberRepository: CoroutineCrudRepository<UserPhoneNumberEntity, Long> {
    suspend fun findAllByUserIdAndIsDeletedFalseOrderById(userId: Long): List<UserPhoneNumberEntity>
    suspend fun findByIdAndUserIdAndIsDeletedFalse(userId: Long, id: Long): UserPhoneNumberEntity?
    suspend fun countByUserIdAndIsDeletedFalse(userId: Long): Long
}
