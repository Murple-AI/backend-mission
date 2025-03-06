package com.murple.murfy.domain.repository

import com.murple.murfy.domain.model.User


interface UserRepository {
    fun save(user: User): User

    fun findById(id: Long): User?

    fun delete(id: Long)

    fun findByName(name: String): List<User>

    fun findByNameOrderByCreatedAtLimit(name: String): List<User>
}
