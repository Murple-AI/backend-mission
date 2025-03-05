package app.boboc.chatserver.repository

import app.boboc.chatserver.entity.UserEntity
import org.springframework.data.domain.Limit
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<UserEntity, Long> {
    suspend fun findAllByNameAndDeletedFalseOrderByCreatedAt(name: String, limit: Limit = Limit.of(5)): List<UserEntity>
    suspend fun findAllByDeletedFalseOrderByCreatedAt(): List<UserEntity>
    suspend fun existsByIdAndDeletedFalse(id: Long): Boolean
}
