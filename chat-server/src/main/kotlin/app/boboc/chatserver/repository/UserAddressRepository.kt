package app.boboc.chatserver.repository

import app.boboc.chatserver.entity.UserAddressEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserAddressRepository: CoroutineCrudRepository<UserAddressEntity, Long> {
    suspend fun findAllByUserIdAndDeletedFalseOrderById(userId: Long): List<UserAddressEntity>
    suspend fun findByIdAndUserIdAndDeletedFalse(userId: Long, id: Long): UserAddressEntity?
    suspend fun countByUserIdAndDeletedFalse(userId: Long): Long
}
