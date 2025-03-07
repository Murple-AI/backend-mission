package com.murple.murfy.domain.user.repository

import com.murple.murfy.domain.user.model.UserAggregate


interface UserRepository {
    fun save(user: UserAggregate): UserAggregate

    fun findById(id: Long): UserAggregate?

    fun delete(id: Long)

    fun findByName(name: String): List<UserAggregate>

    fun findTop5ByNameOrderByCreatedAtAsc(name: String): List<UserAggregate>

    fun findByNamesLimitedByCreatedAt(names: List<String>): List<UserAggregate>
}
