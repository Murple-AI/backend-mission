package com.murple.murfy.infrastructure.user.jpa.repository

import com.murple.murfy.infrastructure.user.jpa.entiy.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<UserEntity, Long> {

    fun findByName(name: String): List<UserEntity>

    fun findTop5ByNameOrderByCreatedAtAsc(name: String): List<UserEntity>
}
