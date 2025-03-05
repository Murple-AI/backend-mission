package app.boboc.chatserver.repository

import app.boboc.chatserver.entity.UserAddressEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserAddressRepository: CoroutineCrudRepository<UserAddressEntity, Long> {
    suspend fun findByUserIdAndDeletedFalseOrderById(userId: Long): List<UserAddressEntity>

}
