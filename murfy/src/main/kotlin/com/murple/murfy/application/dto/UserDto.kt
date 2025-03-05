package com.murple.murfy.application.dto



data class UserDto(
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val phones: List<PhoneDto>? = null,
    val addresses: List<AddressDto>? = null
)
