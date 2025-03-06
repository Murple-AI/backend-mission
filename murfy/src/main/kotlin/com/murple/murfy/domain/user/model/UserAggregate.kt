package com.murple.murfy.domain.user.model

import com.murple.murfy.domain.user.enums.Gender
import java.time.ZonedDateTime


data class UserAggregate(
    val id: Long? = null,
    val name: String,
    val age: Int? = null,
    val gender: Gender? = null,
    val email: String? = null,
    val phones: MutableList<Phone> = mutableListOf(),
    val addresses: MutableList<Address> = mutableListOf(),
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val updatedAt: ZonedDateTime = ZonedDateTime.now()
) {
    init {
        require(name.isNotBlank() && name.length <= 1024) { "The name must not be empty and must be at most 1024 characters long." }
        require(age == null || age >= 0) { "Age must be at least 0 or null." }
        require(email == null || (email.length <= 1024 && isValidEmail(email))) { "Email must be in a valid format and at most 1024 characters long." }
        require(phones.size <= 8) { "A maximum of 8 phone numbers can be registered." }
        require(addresses.size <= 8) { "A maximum of 8 addresses can be registered." }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return emailRegex.matches(email)
    }
}
