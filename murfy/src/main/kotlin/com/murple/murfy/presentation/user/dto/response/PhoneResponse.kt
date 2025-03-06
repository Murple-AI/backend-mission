package com.murple.murfy.presentation.user.dto.response

import com.murple.murfy.domain.user.model.Phone


data class PhoneResponse(
    val id: Long,
    val label: String,
    val number: String,
    val countryCode: String,
    val isVerified: Boolean,
) {
    companion object {
        fun from(model: Phone): PhoneResponse {
            return PhoneResponse(
                id = model.id!!,
                label = model.label.toString(),
                number = model.number,
                countryCode = model.countryCode,
                isVerified = model.isVerified,
            )
        }
    }
}
