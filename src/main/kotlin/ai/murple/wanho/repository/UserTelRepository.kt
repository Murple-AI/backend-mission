package ai.murple.wanho.repository

import ai.murple.wanho.entity.UserTelEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserTelRepository: JpaRepository<UserTelEntity, Long> {
    fun findByUserIdx(userIdx: Long): List<UserTelEntity>
    fun findByUserIdxAndLabelIn(userIdx: Long, labels: List<String>): List<UserTelEntity>
}