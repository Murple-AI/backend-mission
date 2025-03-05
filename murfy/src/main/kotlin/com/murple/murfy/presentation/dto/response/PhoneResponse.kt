package com.murple.murfy.presentation.dto.response

import java.time.ZonedDateTime


data class PhoneResponse(
    val id: Long,
    val label: String,
    val number: String,
    val countryCode: String,
    val isVerified: Boolean,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime? = null
)
