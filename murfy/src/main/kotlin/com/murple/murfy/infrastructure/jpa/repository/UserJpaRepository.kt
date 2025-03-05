package com.murple.murfy.infrastructure.jpa.repository

import com.murple.murfy.infrastructure.jpa.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<UserEntity, Long> {

    fun findByName(name: String): List<UserEntity>

    fun findTop5ByNameOrderByCreatedAtAsc(name: String): List<UserEntity>
}
