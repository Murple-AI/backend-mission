package com.murple.murfy.presentation.dto.response

import java.time.ZonedDateTime


data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val phones: List<PhoneResponse> = emptyList(),
    val addresses: List<AddressResponse> = emptyList(),
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
)
