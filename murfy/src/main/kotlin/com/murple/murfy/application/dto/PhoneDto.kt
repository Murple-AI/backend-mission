package com.murple.murfy.application.dto



data class PhoneDto(
    val label: String,
    val number: String,
    val countryCode: String? = null,
    val isVerified: Boolean = false
)
