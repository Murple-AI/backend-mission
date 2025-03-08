package com.murple.murfy.infrastructure.user.jpa.repository

import com.murple.murfy.infrastructure.user.jpa.entiy.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserJpaRepository : JpaRepository<UserEntity, Long> {


    fun findTop5ByNameOrderByCreatedAtAsc(name: String): List<UserEntity>


    @Query(
        value = """
            WITH RankedUsers AS (
                SELECT u.*, 
                       ROW_NUMBER() OVER (PARTITION BY u.name ORDER BY u.created_at) as rn 
                FROM users u 
                WHERE u.name IN :usernames
            ) 
            SELECT * FROM RankedUsers WHERE rn <= 5
        """,
        nativeQuery = true
    )
    fun findByNamesLimitedByCreatedAt(@Param("usernames") usernames: List<String>): List<UserEntity>
}
