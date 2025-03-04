package com.murple.murfy.domain.model

import java.time.ZonedDateTime


class User(
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
        require(name.isNotBlank() && name.length <= 1024) { "이름은 비어있지 않아야 하며 최대 1024자여야 합니다" }
        require(age == null || age >= 0) { "나이는 0 이상이어야 합니다" }
        require(email == null || (email.length <= 1024 && isValidEmail(email))) { "이메일은 유효한 형식이어야 하며 최대 1024자여야 합니다" }
        require(phones.size <= 8) { "전화번호는 최대 8개까지만 등록할 수 있습니다" }
        require(addresses.size <= 8) { "주소는 최대 8개까지만 등록할 수 있습니다" }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return emailRegex.matches(email)
    }
}
