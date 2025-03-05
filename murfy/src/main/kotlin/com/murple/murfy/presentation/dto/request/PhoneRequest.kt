package com.murple.murfy.presentation.dto.request

import com.murple.murfy.application.dto.PhoneDto


data class PhoneRequest(
    val label: String,
    val number: String,
    val countryCode: String? = null,
    val isVerified: Boolean = false
) {
    fun toServiceDto(): PhoneDto {
        return PhoneDto(
            label = label,
            number = number,
            countryCode = countryCode,
            isVerified = isVerified
        )
    }
}
