package app.boboc.chatserver.repository

import app.boboc.chatserver.entity.UserAddressEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserAddressRepository: CoroutineCrudRepository<UserAddressEntity, Long> {
    suspend fun findAllByUserIdAndIsDeletedFalseOrderById(userId: Long): List<UserAddressEntity>
    suspend fun findByIdAndUserIdAndIsDeletedFalse(userId: Long, id: Long): UserAddressEntity?
    suspend fun countByUserIdAndIsDeletedFalse(userId: Long): Long
}
