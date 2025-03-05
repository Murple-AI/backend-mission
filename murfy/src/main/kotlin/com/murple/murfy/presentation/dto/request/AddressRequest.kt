package com.murple.murfy.presentation.dto.request

import com.murple.murfy.application.dto.AddressDto


data class AddressRequest(
    val label: String,
    val address: String
) {
    fun toServiceDto(): AddressDto {
        return AddressDto(
            label = label,
            address = address
        )
    }
}
