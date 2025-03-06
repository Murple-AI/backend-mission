package com.murple.murfy.application.user.dto


data class UserDto(
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val phones: List<PhoneDto>? = null,
    val addresses: List<AddressDto>? = null
)
