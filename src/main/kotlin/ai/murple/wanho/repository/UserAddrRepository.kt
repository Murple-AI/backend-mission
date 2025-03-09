package ai.murple.wanho.repository

import ai.murple.wanho.entity.UserAddrEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAddrRepository: JpaRepository<UserAddrEntity, Long> {
    fun findByUserIdx(idx: Long): List<UserAddrEntity>
    fun findByUserIdxAndLabelIn(userIdx: Long, labels: List<String>): List<UserAddrEntity>
}