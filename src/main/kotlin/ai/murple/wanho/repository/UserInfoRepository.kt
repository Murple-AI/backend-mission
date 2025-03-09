package ai.murple.wanho.repository

import ai.murple.wanho.code.UserStatusType
import ai.murple.wanho.entity.UserInfoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserInfoRepository: JpaRepository<UserInfoEntity, Long> {
    fun findByIdxAndStatus(idx: Long, status: UserStatusType): UserInfoEntity?
    fun findTop5ByNameOrderByCreatedAtAsc(name : String): List<UserInfoEntity>
}